package reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MimeTypesReader extends Reader{
    public MimeTypesReader(String path){
        try {
            this.reader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            //System.out.println("Error loading MimeTypes");
            throw new RuntimeException(e);
        }
    }
    public MimeTypesReader(){
        this("conf/mime.types");
    }
    @Override
    public MimeTypes read() throws IOException {
        MimeTypes mimeTypeData = new MimeTypes();
        String line = reader.readLine();
        while (line != null) {
            if(line.trim().length() != 0 && !line.startsWith("#")){
                String[] split = line.split("\\s+");
                for (int i = 1; i < split.length; i++) {
                    mimeTypeData.add(split[i],split[0]);
//                    System.out.println("Extension: "+ split[i]+ "  Type"+ split[0]);
                }
            }
            line = reader.readLine();
        }
        return mimeTypeData;
    }
}

