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
        config.setListen(8080);

        while (line != null) {
            if (line.trim().length() != 0) {
                if (line.startsWith("#") || line.startsWith("//")) {
                    //System.out.println("Comment: " + line);
                }
                else if (line.startsWith("DocumentRoot")){
//                    System.out.print("Document Root: ");
//                    int indexQuoteStart = line.indexOf("\"");
//                    int indexQuoteEnd = line.indexOf("\"", indexQuoteStart + 1);
//                    System.out.println(line.substring(indexQuoteStart + 1, indexQuoteEnd));
//                    String[] splitLine = line.split("\\s+");
//                    System.out.println(Arrays.toString(splitLine));
                    String path = line.substring(line.indexOf("\"") + 1);
                    path = path.substring(0, path.indexOf("\""));
                    //System.out.println(root);
                    config.setDocumentRoot(path);
                }
                else if (line.startsWith("Listen")){
//                    System.out.print("Listen: ");
                    String[] splitLine = line.split("\\s+");
//                    System.out.println(Arrays.toString(splitLine));
                    config.setListen(Integer.parseInt(splitLine[1].trim()));
                }
                else if (line.startsWith("LogFile")){
//                    System.out.print("Logging.Log File: ");
//                    String[] splitLine = line.split("\\s+");
                    String path = line.substring(line.indexOf("\"") + 1);
                    path = path.substring(0, path.indexOf("\""));
//                    System.out.println(Arrays.toString(splitLine));
                    config.setLogFile(path);
                }
                else if (line.startsWith("ScriptAlias")) {
//                    System.out.print("Script Alias: ");
                    String[] splitLine = line.split("\\s+");

                    String path = line.substring(line.indexOf("\"") + 1);
                    path = path.substring(0, path.indexOf("\""));

//                    System.out.println(Arrays.toString(splitLine));
                    config.addScriptAlias(splitLine[1].trim(), path);
                }
                else if (line.startsWith("Alias")) {
                    String[] splitLine = line.split("\\s+");
                    String path = line.substring(line.indexOf("\"") + 1);
                    path = path.substring(0, path.indexOf("\""));
                    config.addAlias(splitLine[1].trim(), path);
                }
                else if (line.startsWith("DirectoryIndex")) {
//                    System.out.print("Directory Index: ");
                    String[] splitLine = line.split("\\s+");
//                    System.out.println(Arrays.toString(splitLine));
                    String path = line.substring(line.indexOf("\"") + 1);
                    path = path.substring(0, path.indexOf("\""));
                    config.setDirectoryIndex(path);
                } else {
                    System.out.print("Not Supported: ");
                    String[] splitLine = line.split("\\s+");
                    System.out.println(Arrays.toString(splitLine));
                    //throw new RuntimeException("HTTPD Parse Error");
                }
            }
            if (config.getDirectoryIndex() == null) {
                config.setDirectoryIndex("index.html");
            }
            line = reader.readLine();
        }


        return config;
    }

//    public static void main(String[] args) {
//        ConfReader test = new ConfReader("conf/httpd.conf");
//    }
}
