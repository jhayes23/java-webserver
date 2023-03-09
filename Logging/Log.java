package Logging;

import ResponseRequest.HTTPMessage;
import ResponseRequest.ResponseCode;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Log{
    private final PrintWriter writer;

    protected String ip;
    protected String ident;
    protected String authuser;

    protected ZonedDateTime date;

    protected HTTPMessage.RequestStartLine request;

    protected ResponseCode status;

    protected int bytes;

    public String getIp() {
        return ip;
    }

    public Log setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getIdent() {
        return ident;
    }

    public Log setIdent(String ident) {
        this.ident = ident;
        return this;
    }

    public String getAuthuser() {
        return authuser;
    }

    public Log setAuthuser(String authuser) {
        this.authuser = authuser;
        return this;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Log setDate(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public HTTPMessage.RequestStartLine getRequest() {
        return request;
    }

    public Log setRequest(HTTPMessage.RequestStartLine request) {
        this.request = request;
        return this;
    }

    public ResponseCode getStatus() {
        return status;
    }

    public Log setStatus(ResponseCode status) {
        this.status = status;
        return this;
    }

    public int getBytes() {
        return bytes;
    }

    public Log setBytes(int bytes) {
        this.bytes = bytes;
        return this;
    }

    public Log(String path){
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

    public void out() {
        String outputIP = this.ip != null ? this.ip : "-";
        String outputIdent = this.ident != null ? this.ident : "-";
        String outputAuthUser = this.authuser != null ? this.authuser : "-";
        ZonedDateTime currDate = ZonedDateTime.now();
        String outputDate = "[" + currDate.format(DateTimeFormatter.ofPattern("d/MMM/YYYY:HH:mm:ss Z")) + "]";
        String outputRequest = this.request != null ? "\"" + this.request.toString() + "\"" : "-";
        String outputStatus = this.status != null ? String.valueOf(this.status.code) : "-";
        String output = String.format("%s %s %s %s %s %s %s",
                outputIP,
                outputIdent,
                outputAuthUser,
                outputDate,
                outputRequest,
                outputStatus,
                String.valueOf(this.bytes)
                );
        System.out.println(output);
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
