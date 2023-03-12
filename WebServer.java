import Logging.Log;
import ResponseRequest.*;
import reader.ConfData;
import reader.ConfReader;
import reader.HTTPReader;
import ResponseRequest.HTTPMessage;
import ResponseRequest.ResponseWriter;
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
        ConfReader confReader = new ConfReader(confPath);
        try {
            MimeTypes mimeTypes = new MimeTypesReader().read();
            ConfData config = confReader.read();
            ServerSocket server = new ServerSocket(config.getListen());
//            Log log = new Log(config.getLogFile());
//            log
//                    .setIp("127.0.0.1")
//                    .setAuthuser("frank")
//                    .setRequest(new HTTPMessage.RequestStartLine(
//                            HTTPMethod.GET,
//                            "/apache_pb.gif",
//                            "HTTP/1.0"))
//                    .setStatus(ResponseCode.OK)
//                    .setBytes(2326)
//                    .out();
            System.out.println("Server started...");
            Socket socket;

            while ((socket = server.accept()) != null) {
                Thread thread = new Thread(new RequestHandler(socket, config, mimeTypes));
                thread.start();
                System.out.println("Thread " + thread.getId());
//                HTTPReader httpReader = new HTTPReader(socket);
//                ResponseWriter handler = new ResponseWriter(socket);
//                HTTPMessage request;
//                request = httpReader.read();
//                ResponseRequest.RequestProcessor processor = new ResponseRequest.RequestProcessor(config.getDocumentRoot(), config.getDirectoryIndex(), config.getScriptAlias(), request);
//                handler.write("HTTP/1.1 " + processor.processReq() + "\r\n");
//                handler.write("Date: " + dateTime.format(new Date()) + "\r\n");
//                handler.write("Server: Shi, Hayes\r\n");
//                if (!processor.isScript()) {
//                    if(processor.isAuthRequired()){
//                        handler.write("WWW-Authenticate: "+ processor.getAuthType()+ " realm="+ processor.getAuthName()+"\r\n");
//                    }else{
//                        if(processor.hasResources()){
//                            handler.write("Content-Length: " + processor.getResourceSize() + "\r\n");
//                            handler.write("Content-Type: " + mimeTypes.get(processor.getExtension()) + "\r\n");
//                            handler.write("\r\n");
//                            handler.write(processor.getResource());
//                        }
//                    }
//                }else{
//                    handler.write(processor.getResource());
//                }
//                handler.flush();
//                socket.close();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
