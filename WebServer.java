import ResponseRequest.HTTPMessage;
import ResponseRequest.RequestHandler;
import reader.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


public class WebServer {
    private static final SimpleDateFormat dateTime = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss Z");

    public static void main(String[] args) {
        // This file will be compiled by script and must be at
        // the root of your project directory

        String confPath = "conf/httpd.conf";
        String mimePath = "conf/httpd.mimetypes";
        ConfReader confReader = new ConfReader(confPath);
        try {
            MimeTypes mimeTypes = new MimeTypesReader().read();
            ConfData config = confReader.read();
            ServerSocket server = new ServerSocket(config.getListen());
            System.out.println("Server started...");
            Socket socket;

            while ((socket = server.accept()) != null) {
                HTTPReader httpReader = new HTTPReader(socket);
                RequestHandler handler = new RequestHandler(socket);
                HTTPMessage request;
                request = httpReader.read();
                RequestProcessor processor = new RequestProcessor(config.getDocumentRoot(), config.getScriptAlias(), request, httpReader);
                handler.write("HTTP/1.1 " + processor.processReq() + "\n");
                handler.write("Date: " + dateTime.format(new Date()) + "\n");
                handler.write("Server: Shi Hayes\n");
                if (!processor.isScript()) {
                    handler.write("Content-Type: " + mimeTypes.get(processor.getExtension()) + "\n");
                    handler.write("Content-Length: " + processor.getResourceSize() + "\n");
                    handler.write("\n");
                    if (processor.hasResources()){handler.write(processor.getResource());}
                }
                handler.flush();
                socket.close();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
