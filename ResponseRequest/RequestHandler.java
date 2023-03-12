package ResponseRequest;

import Logging.Log;
import reader.ConfData;
import reader.HTTPReader;
import reader.MimeTypes;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestHandler implements Runnable{

    Socket socket;
    ConfData config;

    MimeTypes mimeTypes;

    private static final SimpleDateFormat dateTime = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss Z");

    public RequestHandler(Socket socket, ConfData config, MimeTypes mimeTypes) {
        this.socket = socket;
        this.config = config;
        this.mimeTypes = mimeTypes;
    }

    @Override
    public void run() {
        try {
            HTTPReader httpReader = new HTTPReader(socket);
            ResponseWriter handler = new ResponseWriter(socket);
            HTTPMessage request;
            request = httpReader.read();
            RequestProcessor processor = new RequestProcessor(config.getDocumentRoot(), config.getDirectoryIndex(), config.getScriptAlias(), request);
            ResponseCode reqResult = processor.processReq();

            handler.write("HTTP/1.1 " + reqResult + "\r\n");
            handler.write("Date: " + dateTime.format(new Date()) + "\r\n");
            handler.write("Server: Shi, Hayes\r\n");
            if (!processor.isScript()) {
                if (processor.isAuthRequired()) {
                    handler.write("WWW-Authenticate: " + processor.getAuthType() + " realm=" + processor.getAuthName() + "\r\n");
                } else {
                    if (processor.hasResources()) {
                        handler.write("Content-Length: " + String.valueOf(processor.getResourceSize()) + "\r\n");
                        handler.write("Content-Type: " + mimeTypes.get(processor.getExtension()) + "\r\n");
                        handler.write("\r\n");
                        handler.write(processor.getResource());
                    }
                }
            } else {
                handler.write(processor.getResource());
            }
            Log log = new Log(config.getLogFile());
            log
                    .setAuthuser(processor.getAuthName())
                    .setRequest(request.getStartLine())
                    .setStatus(reqResult)
                    .setBytes(processor.getResourceSize())
                    .out();
            handler.flush();
            socket.close();
        } catch(IOException | InterruptedException e){
            throw new RuntimeException(e);
        }
    }
}
