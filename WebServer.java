
import ResponseRequest.*;
import reader.ConfData;
import reader.ConfReader;
import reader.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    //private static final SimpleDateFormat dateTime = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss Z");

    public static void main(String[] args) {
        // This file will be compiled by script and must be at
        // the root of your project directory

        String confPath = "conf/httpd.conf";
        ConfReader confReader = new ConfReader(confPath);
        try {
            MimeTypes mimeTypes = new MimeTypesReader().read();
            ConfData config = confReader.read();
            ServerSocket server = new ServerSocket(config.getListen());
            Socket socket;

            while ((socket = server.accept()) != null) {
                Thread thread = new Thread(new RequestHandler(socket, config, mimeTypes));
                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
