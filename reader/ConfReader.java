package reader;

import java.io.*;
import java.util.Arrays;

public class ConfReader extends Reader {

    public ConfReader(String path) {
        try {
            this.reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();

            while(reader.ready()){
                if(line.trim().length() !=0 && !line.startsWith("#")){
                    String[] splitLine = line.split("\\s+");

                    System.out.println(Arrays.toString(splitLine));
                }
                line = reader.readLine();
            }

        } catch (IOException e) {
            System.out.println("Error initializing conf reader. File not found.");
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ConfReader test = new ConfReader("conf/httpd.conf");
    }
}
