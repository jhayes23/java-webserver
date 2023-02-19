package reader;

import java.io.*;

public class ConfReader extends Reader {

    public ConfReader(String path) {
        try {
            this.reader = new BufferedReader(new FileReader(path));
            System.out.println("Success");
        } catch (FileNotFoundException e) {
            System.out.println("Error initializing conf reader. File not found.");
            throw new RuntimeException(e);
        }
    }
}
