package ResponseRequest;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ResponseHandler {

    private BufferedWriter responseWriter;
    public ResponseHandler(Socket socket) {
        try {
            this.responseWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Error initializing HTTP output.");
            throw new RuntimeException(e);
        }
    }

    public void write() {

    }

}
