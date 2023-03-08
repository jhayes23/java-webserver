import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Log{
    private final PrintWriter writer;

    Log(String path){

        Path file = Paths.get(path);
            try {
                if(!Files.exists(file)){
                    Files.createDirectories(file.toAbsolutePath().getParent());
                    Files.createFile(file);
                }
                    writer = new PrintWriter(new FileOutputStream(path,true),true);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
    }

    public void out(String response, String date){
        String output = String.format("[TODO: IP ADDRESS] - - [%s] %s", date,response);
        System.out.println(output);
        writer.println(output);
    }

    public void out(String response,String date, String userId){
        String output = String.format("[TODO: IP ADDRESS] - %s [%s] %s",userId,date,response);
        System.out.println(output);
        writer.println(output);
    }

    public void closeLog(){
        writer.close();
    }
}
