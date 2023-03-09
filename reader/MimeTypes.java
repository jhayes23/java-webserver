package reader;

import java.util.HashMap;
import java.util.Map;

public class MimeTypes {
    private Map<String ,String> mimeTypes;
    public MimeTypes(){
        mimeTypes = new HashMap<>();
    }
    public String get(String extension){
        if (mimeTypes.containsKey(extension)) {
            return mimeTypes.get(extension);
        }
        return "text/text";
    }
    public void add(String extension, String type){
        mimeTypes.put(extension,type);
    }
}
