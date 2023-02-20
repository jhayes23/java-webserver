import reader.ConfData;
import reader.ConfReader;

import java.io.IOException;
import java.net.ServerSocket;

public class WebServer {
  public static void main(String[] args) {
    // This file will be compiled by script and must be at
    // the root of your project directory

    String confPath = "conf/httpd.conf";
    ConfReader confReader = new ConfReader(confPath);
    try {
      ConfData config = confReader.read();
      ServerSocket socket = new ServerSocket(config.getListen());
      System.out.println("Server started...");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
