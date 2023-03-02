package reader;

import ResponseRequest.HTTPMessage;
import ResponseRequest.HTTPMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class HTTPReader extends Reader {
    public HTTPReader(Socket socket) {
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Error initializing HTTP reader.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public HTTPMessage read() throws IOException {
        String line = reader.readLine();
        if (line == null) {
            return null;
        }
        HTTPMessage request = new HTTPMessage();
        //System.out.println(line);
        String[] startLineSplit = line.split(" ");
        String method = startLineSplit[0].trim().toUpperCase();
        HTTPMethod methodEnum;
        switch(method) {
            case "GET":
                methodEnum =  HTTPMethod.GET;
                break;
            case "HEAD":
                methodEnum =  HTTPMethod.HEAD;
                break;
            case "POST":
                methodEnum =  HTTPMethod.POST;
                break;
            case "PUT":
                methodEnum =  HTTPMethod.PUT;
                break;
            case "DELETE":
                methodEnum =  HTTPMethod.DELETE;
                break;
            default:
                methodEnum = null;
        }
        request.setStartLine(new HTTPMessage.RequestStartLine(methodEnum, startLineSplit[1].trim(), startLineSplit[2].trim()));
        line = reader.readLine();
        while (!line.trim().equals("")) {
            //System.out.println(line);
            String[] headerLineSplit = line.split(":");
            request.addHeader(headerLineSplit[0].trim(), headerLineSplit[1].trim());
            line = reader.readLine();
        }
        //line = reader.readLine();
        while (line != null && !line.trim().equals("")) {
            request.setBody(request.getBody() + "\n" + line);
            line = reader.readLine();
        }
        return request;
    }
}
