package ResponseRequest;

import java.io.*;
import java.net.Socket;

public class RequestHandler {

    private DataOutputStream dataout;
    public RequestHandler(Socket socket) {
        try {
            dataout = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error initializing HTTP output.");
            throw new RuntimeException(e);
        }
    }

    public void write(String input) throws IOException {
        dataout.writeBytes(input);
        System.out.print(input);
    }
    public void write(byte[] input) throws IOException {
        dataout.write(input);
        //System.out.println(input);
    }

    public void flush() throws IOException {
        dataout.flush();
    }

}
