package reader;

import ResponseRequest.HTTPMessage;
import ResponseRequest.HTTPMethod;

import java.io.*;
import java.net.Socket;

public class HTTPReader extends Reader {

    InputStream stream;
    InputStreamReader streamReader;
    public HTTPReader(Socket socket) {
        try {
            this.stream = socket.getInputStream();
            this.streamReader = new InputStreamReader(stream);
            this.reader = new BufferedReader(streamReader);
        } catch (IOException e) {
            System.out.println("Error initializing HTTP reader.");
            throw new RuntimeException(e);
        }
    }

/*    @Override
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
        int contentLen = 0;
        while (!line.trim().equals("")) {
            String[] headerLineSplit = line.split(": ");
            request.addHeader(headerLineSplit[0].trim(), headerLineSplit[1].trim());
            if (headerLineSplit[0].equals("Content-Length")) {
                contentLen = Integer.parseInt(headerLineSplit[1]);
            }
            line = reader.readLine();
        }

//        ByteArrayOutputStream bodyStream = new ByteArrayOutputStream();
//        byte[] buffer = new byte[4096];
//        int bytesRead = 0;
//        while (bytesRead < contentLen) {
//            int numRead = reader.read();
//        }
//        while ((bytesRead = reader.read()) != -1) {
//            bodyStream.write(buffer, 0, bytesRead);
//        }
//        request.setBody(bodyStream.toByteArray());

//        if(contentLen >0){
////            char[] buff = new char[contentLen];
////            reader.read(buff, 0, contentLen);
////            String body = String.valueOf(buff);
//            //request.setBody(body);
//            char[] buffer = new char[1024];
//            int bytesRead = 0;
//            StringBuilder bodyBuilder = new StringBuilder();
//            while (bytesRead < contentLen) {
//                int numToRead = Math.min(buffer.length, contentLen - bytesRead);
//                int numRead = reader.read(buffer, 0, numToRead);
//                if (numRead == -1) {
//                    // End of stream reached prematurely
//                    break;
//                }
//                bytesRead += numRead;
//                bodyBuilder.append(buffer, 0, numRead);
//            }
//            request.setBody(bodyBuilder.toString());
//        }

        return request;
    }
 */

    public HTTPMessage read() throws IOException {
        StringBuilder line = new StringBuilder();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];
        while(true) {
            int n = stream.read(buffer);
            if( n < 0 ) break;
            outputStream.write(buffer,0,n);
        }
        byte data[] = outputStream.toByteArray();
//        do {
//            line.append((char) streamReader.read());
//            System.out.println(line);
//        } while (stream.available() > 0);
//        if (line == null) {
//            return null;
//        }
        if (outputStream.size() == 0) {
            return null;
        }
        System.out.println(data.toString());
        HTTPMessage request = new HTTPMessage();
        return request;
    }
}
