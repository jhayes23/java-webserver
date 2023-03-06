package ResponseRequest;

import java.io.*;
import java.net.Socket;

public class RequestHandler {

//    private PrintWriter responseWriter;
    private DataOutputStream dataout;
    public RequestHandler(Socket socket) {
        try {
            dataout = new DataOutputStream(socket.getOutputStream());
//            this.responseWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            System.out.println("Error initializing HTTP output.");
            throw new RuntimeException(e);
        }
    }

    public void write(String input) throws IOException {
        System.out.print(input);
//        responseWriter.write(input);
        dataout.writeBytes(input);
        //System.out.println(input);
    }
    public void write(byte[] input) throws IOException {
//        responseWriter.write();
        dataout.write(input);
        //System.out.println(input);
    }

    public void flush() throws IOException {
        dataout.flush();
    }

}
