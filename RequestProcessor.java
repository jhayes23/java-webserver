import ResponseRequest.HTTPMessage;
import ResponseRequest.ResponseCode;
import reader.Htpassword;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RequestProcessor {
    private final String docRoot;
    private String method, target, body, queryString;
    private final HashMap<String, String> alias;
    private final HashMap<String, String> headers;
    private long fileSize;
    private Path filePath;
    private boolean isScript = false;
    private boolean sendFile = false;
    private boolean aliased = false;
    private boolean authRequired = false;
    private Htpassword htpassword;
    private byte[] bytes;
    private Path parentDirectory;
    private String authFile, authType, authName;

    RequestProcessor(String documentRoot, HashMap<String, String> alias, HTTPMessage request) throws IOException {
        this.docRoot = documentRoot;
        this.alias = alias;
        this.headers = request.getHeaders();
        String[] split = request.getStartLine().toString().split("\\s+");
        this.method = split[0];
        this.target = split[1];
        //this.body = request.getBody();


    }

    public void resolvePath(String pathReq) {
        StringBuilder builder = new StringBuilder(docRoot);
        if (pathReq == null || pathReq.equals("/")) {
            builder.append("index.html");
        } else {
            pathReq = pathReq.replaceFirst("/", "");
            if (pathReq.contains("?")) {
                int queryStart = pathReq.indexOf("?");
                queryString = pathReq.substring(queryStart + 1);
                pathReq = pathReq.substring(0, queryStart);
            }
            builder.append(aliasCheck(pathReq));
        }
        filePath = Paths.get(builder.toString());
        if (Files.isDirectory(filePath)) {
            filePath = Path.of(filePath + "/index.html");
        }
        parentDirectory = filePath.toAbsolutePath().getParent();
        if (Files.isExecutable(filePath)) {
            isScript = true;
        }

    }

    public ResponseCode processReq() throws IOException, InterruptedException {
        System.out.println("Method: " + method + "  Request: " + target);
        this.resolvePath(target);
        if (Files.exists(Path.of(parentDirectory + "/.htaccess"))) {
            authRequired = true;
            loadHtAccess(parentDirectory + "/.htaccess");
            if (!headers.containsKey("Authorization")) {
                return ResponseCode.UNAUTHORIZED;
            }
            htpassword = new Htpassword(authFile);
            if (!authenticated()) {
                return ResponseCode.FORBIDDEN;
            }
        }

        if (method.equals("PUT")) {
            Files.createDirectories(parentDirectory);
            Files.createFile(filePath);
            if (body != null) {
                Files.write(filePath, body.getBytes());
            }
            return ResponseCode.CREATED;
        } else {
            if (!Files.exists(filePath)) {
                return ResponseCode.NOT_FOUND;
            }
            this.loadFileSize();
            if (isScript()) {
                return executeScript() ? ResponseCode.OK : ResponseCode.INTERNAL_SERVER_ERROR;
            } else {
                switch (method) {
                    case "DELETE":
                        Files.deleteIfExists(filePath);
                        return ResponseCode.NO_CONTENT;
                    case "POST":
                        sendFile = true;
                        this.loadResource();
                        return ResponseCode.OK;
                    case "GET":
                        if(!wasModifiedSince()) {
                            return ResponseCode.NOT_MODIFIED;
                        }
                        else{
                            sendFile = true;
                            this.loadResource();
                            return ResponseCode.OK;
                        }

                    case "HEAD":
                        return ResponseCode.OK;
                    default:
                        return ResponseCode.INTERNAL_SERVER_ERROR;
                }
            }
        }
    }

    private void loadResource() {
        try {
            bytes = Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean authenticated() {
        if (headers.containsKey("Authorization")) {
            String[] split = headers.get("Authorization").split("\\s+", 2);
            if (split[0].equals("Basic")) {
                if (htpassword.isAuthorized(split[1])) {
                    authRequired = false;
                    return true;
                }
            }
        }

        return false;
    }
    private String aliasCheck(String resourcePath) {
        String path = "/" + resourcePath;

        for (String key : alias.keySet()) {
            if (path.contains(key)) {
                String temp = alias.get(key).replaceFirst("/", "");
                path = resourcePath.replace(key, temp);
                aliased = true;
            }
        }
        return path;
    }

    private void loadFileSize() {
        try {
            fileSize = Files.size(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isScript() {
        return isScript && aliased;
    }

    public String getResourceSize() {
        return String.valueOf(fileSize);
    }

    public byte[] getResource() {
        return bytes;
    }

    public String getExtension() {
        int last = filePath.toString().lastIndexOf('.') + 1;
        return filePath.toString().substring(last);
    }

    public boolean hasResources() {
        return sendFile;
    }

    private boolean executeScript() {



        try {
            ProcessBuilder jBuild = new ProcessBuilder(String.valueOf(filePath));
            Map<String, String> environment = jBuild.environment();
            environment.put("SERVER_PROTOCOL", "HTTP/1.1");
            if (queryString != null) {
                environment.put("QUERY_STRING", queryString);
            }
            for (Map.Entry<String, String> header : headers.entrySet()) {
                environment.put(
                        "HTTP_" + header.getKey().toUpperCase()
                                .replaceAll("-", "_"), headers.get(header.getKey()));
            }
            Process process = jBuild.start();
            DataOutputStream writer = new DataOutputStream(process.getOutputStream());
            if(body!=null){
                writer.write(body.getBytes());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\r\n");
            }
            response.append("\r\n");
            sendFile = true;
            bytes  = response.toString().getBytes();
        } catch (IOException e) {
            System.out.println("CGI ERROR");
            return false;
        }
        return true;
    }

    private void loadHtAccess(String path) throws RuntimeException {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            while (line != null) {
                String[] split = line.trim().split("\\s+", 2);
                switch (split[0]) {
                    case "AuthUserFile" -> authFile = split[1].
                            replaceFirst("\"", "").replaceFirst("\"", "/");
                    case "AuthType" -> authType = split[1];
                    case "AuthName" -> authName = split[1];
                }
                line = reader.readLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAuthType() {
        return authType;
    }

    public String getAuthName() {
        return authName;
    }

    public boolean isAuthRequired() {
        return authRequired;
    }

    private boolean wasModifiedSince() {
        long serverModDate;
        long clientModDateTime;
        if (headers.containsKey("If-Modified-Since")) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
            String respDate = headers.get("If-Modified-Since");
            try {
                Date dateParse = sdf.parse(respDate);

                clientModDateTime = dateParse.getTime();
                serverModDate = Files.getLastModifiedTime(filePath).toMillis();
                return serverModDate >= clientModDateTime;
            } catch (ParseException | IOException e) {
                throw new RuntimeException(e);
            }

        }
        return true;
    }
}