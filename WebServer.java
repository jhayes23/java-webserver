import ResponseRequest.HTTPMessage;
import reader.ConfData;
import reader.ConfReader;
import reader.HTTPReader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class WebServer {
    public static void main(String[] args) {
        // This file will be compiled by script and must be at
        // the root of your project directory

        String confPath = "conf/httpd.conf";
        ConfReader confReader = new ConfReader(confPath);
        try {
            ConfData config = confReader.read();
            ServerSocket server = new ServerSocket(config.getListen());
            System.out.println("Server started...");

            while (true) {
                Socket socket = server.accept();
                HTTPReader httpReader = new HTTPReader(socket);
                HTTPMessage request;
                if (httpReader.read() == null) { //works around first socket read null
                    socket.close();
                } else {
                    request = httpReader.read();
                }
//                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                String line = in.readLine();
//                //System.out.println(line);
//                if (line != null) {
//                    System.out.println(Arrays.toString(line.split(" / ")));
//                }
//                line = in.readLine();
//                while (line != null) {
//                    System.out.println(line);
//                    line = in.readLine();
//                }
                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
