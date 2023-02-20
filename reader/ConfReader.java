package reader;

import java.io.*;
import java.util.Arrays;

public class ConfReader extends Reader {

    public ConfReader(String path) {
        try {
            this.reader = new BufferedReader(new FileReader(path));
        } catch (IOException e) {
            System.out.println("Error initializing conf reader. File not found.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public ConfData read() throws IOException {
        String line = reader.readLine();
        ConfData config = new ConfData();

        while (line != null) {
            if (line.trim().length() != 0) {
                if (line.startsWith("#") || line.startsWith("//")) {
                    //System.out.println("Comment: " + line);
                }
                else if (line.startsWith("DocumentRoot")){
//                    System.out.print("Document Root: ");
                    String[] splitLine = line.split("\\s+");
//                    System.out.println(Arrays.toString(splitLine));
                    config.setDocumentRoot(splitLine[1].trim());
                }
                else if (line.startsWith("Listen")){
//                    System.out.print("Listen: ");
                    String[] splitLine = line.split("\\s+");
//                    System.out.println(Arrays.toString(splitLine));
                    config.setListen(Integer.parseInt(splitLine[1].trim()));
                }
                else if (line.startsWith("LogFile")){
//                    System.out.print("Log File: ");
                    String[] splitLine = line.split("\\s+");
//                    System.out.println(Arrays.toString(splitLine));
                    config.setLogFile(splitLine[1].trim());
                }
                else if (line.startsWith("ScriptAlias") || line.startsWith("Alias")) {
//                    System.out.print("Script Alias: ");
                    String[] splitLine = line.split("\\s+");
//                    System.out.println(Arrays.toString(splitLine));
                    config.addScriptAlias(splitLine[1].trim(), splitLine[2].trim());
                }
                else if (line.startsWith("DirectoryIndex")) {
//                    System.out.print("Directory Index: ");
                    String[] splitLine = line.split("\\s+");
//                    System.out.println(Arrays.toString(splitLine));
                    config.setDirectoryIndex(splitLine[1].trim());
                } else {
                    System.out.print("Not Supported: ");
                    String[] splitLine = line.split("\\s+");
                    System.out.println(Arrays.toString(splitLine));
                    //throw new RuntimeException("HTTPD Parse Error");
                }
            }
            line = reader.readLine();
        }


        return config;
    }

//    public static void main(String[] args) {
//        ConfReader test = new ConfReader("conf/httpd.conf");
//    }
}
