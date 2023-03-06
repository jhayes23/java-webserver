import ResponseRequest.HTTPMessage;
import ResponseRequest.ResponseCode;
import reader.HTTPReader;
import reader.Htpassword;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class RequestProcessor {
    private final String docRoot;
    private String method, target , body;
    private final HashMap<String,String> alias;
    private long fileSize;
    private  Path filePath;
    private boolean isScript = false;
    private boolean sendFile = false;
    private Htpassword htpassword;
    private byte[] bytes;

    RequestProcessor(String documentRoot, HashMap<String,String> alias, HTTPMessage request, HTTPReader httpReader){ //TODO remove httpReader
        this.docRoot = documentRoot; //from conf
        this.alias = alias;  //from conf
        this.method = "GET"; //TODO Load from request obj
        this.target = httpReader.getHelper(); //TODO target
        this.body = null; //TODO BODY


    }

    public void resolvePath(String pathReq){
        //TODO fix docRoot in confData then move httppassword to constructor
        StringBuilder builder = new StringBuilder(docRoot.replaceFirst("\"","").replaceFirst("\"","/").trim());
        try {
            htpassword = new Htpassword(builder+ ".htpasswd");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(pathReq == null || pathReq.equals("/")){
            builder.append("index.html");
        }else{
            pathReq = pathReq.replaceFirst("/","");
            builder.append(aliasCheck(pathReq));
        }
        filePath = Paths.get(builder.toString());

    }

    public ResponseCode processReq() throws IOException {
        System.out.println("Request: " +target);
        this.resolvePath(target);
        if(!isScript){
            this.loadFileSize();
        }


        if(method.equals("PUT")){
            Path temp = Paths.get(target);
            if(!Files.exists(temp)){
                Files.createDirectories(temp.toAbsolutePath().getParent());
                Files.createFile(temp);
                //TODO FILL BODY in new file
                return ResponseCode.CREATED;
            }
        }else{
            if(!Files.exists(filePath)){ return ResponseCode.NOT_FOUND; }
            if(isScript()){
                 return executeScript()? ResponseCode.OK:ResponseCode.INTERNAL_SERVER_ERROR;
            }else{
                switch (method) {
                    case "DELETE":
                        Files.deleteIfExists(filePath);
                        return ResponseCode.NO_CONTENT;
                    case "POST":
                        sendFile = true;
                        this.loadResource();
                        return ResponseCode.OK;
                    case "GET":
                        sendFile = true;
                        this.loadResource();
                        //compare last modified date from request header against current modified in directory
                        System.out.println("DIFFERENT");
                        return ResponseCode.OK;
                    case "HEAD":
                        return ResponseCode.OK;
                    default:
                        return ResponseCode.INTERNAL_SERVER_ERROR;
                }
            }
        }
        return  ResponseCode.INTERNAL_SERVER_ERROR;
    }

    private void loadResource(){

        try {
            bytes = Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String aliasCheck(String resourcePath){
        String path = "/"+resourcePath;

        for (String key: alias.keySet()){
            if (path.contains(key)){
                //TODO ScriptAlias vs Alias
                isScript = true;
                String temp = alias.get(key).replaceFirst("/","");
                path = resourcePath.replace(key,temp);
            }
        }
        return path;
    }
    private void loadFileSize(){
        try {
            fileSize = Files.size(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean isScript(){return isScript;}
    public String getResourceSize(){return String.valueOf(fileSize);}
    public byte[] getResource() {return bytes;}
    public String getExtension(){
        int last = filePath.toString().lastIndexOf('.')+1;
        return filePath.toString().substring(last);
    }
    public boolean hasResources(){return sendFile;}

    private boolean executeScript(){
        //TODO CGI
        ProcessBuilder jBuild = new ProcessBuilder(String.valueOf(filePath));
        try {
            jBuild.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}