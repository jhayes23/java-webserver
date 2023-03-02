package ResponseRequest;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class RequestHandler {

    private PrintWriter responseWriter;
    public RequestHandler(Socket socket) {
        try {
            this.responseWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            System.out.println("Error initializing HTTP output.");
            throw new RuntimeException(e);
        }
    }

    public void write(String input) {
        String nf = "HTTP/1.1 404 Not Found\n\n";
        responseWriter.write(nf);
        //System.out.println(input);
    }

}
