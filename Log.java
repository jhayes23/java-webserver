import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log{
    private final PrintWriter writer;
    private final SimpleDateFormat dateTime;
    Log(String path){
        dateTime = new SimpleDateFormat("[dd/MM/yyyy:HH:mm:ss Z]");
        Path file = Paths.get(path);
            try {
                if(!Files.exists(file)){
                    int endOfDirectory = path.lastIndexOf('/');
                    Path directory = Paths.get(path.substring(0,endOfDirectory));
                    Files.createDirectories(directory);
                    Files.createFile(file);
                }
                    writer = new PrintWriter(new FileOutputStream(path,true),true);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
    }

    public void out(String response){
        String output = String.format("[TODO: IP ADDRESS] - - %s %s", dateTime.format(new Date()),response);
        System.out.println(output);
        writer.println(output);
    }

    public void out(String response, String userId){
        String output = String.format("[TODO: IP ADDRESS] - %s %s %s",userId,dateTime.format(new Date()),response);
        System.out.println(output);
        writer.println(output);
    }

    public void closeLog(){
        writer.close();
    }
//    public static void main(String[] args) {
//        Log log = new Log("./test/out.txt");
//        log.out("this is a test");
//    }
}
