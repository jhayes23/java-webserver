package reader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ConfData {

    private int listen;
    private String documentRoot;
    private String logFile;
    private HashMap<String, String> scriptAlias;
    private String directoryIndex;

    public ConfData() {
    }

    public int getListen() {
        return listen;
    }

    public void setListen(int listen) {
        this.listen = listen;
    }

    public String getDocumentRoot() {
        return documentRoot;
    }

    public void setDocumentRoot(String documentRoot) {
        this.documentRoot = documentRoot;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public HashMap<String, String> getScriptAlias() {
        return scriptAlias;
    }

    public void addScriptAlias(String symbPath, String absPath) {
        if (scriptAlias == null) {
            scriptAlias = new HashMap<String, String>();
        }
        scriptAlias.put(symbPath, absPath);
    }

    public String getDirectoryIndex() {
        return directoryIndex;
    }

    public void setDirectoryIndex(String directoryIndex) {
        this.directoryIndex = directoryIndex;
    }

    @Override
    public String toString() {
        return "ConfData{" +
                "listen=" + listen +
                ", documentRoot='" + documentRoot + '\'' +
                ", logFile='" + logFile + '\'' +
                ", scriptAlias=" + scriptAlias +
                ", directoryIndex='" + directoryIndex + '\'' +
                '}';
    }
}
