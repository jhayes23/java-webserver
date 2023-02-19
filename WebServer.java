import reader.ConfReader;

public class WebServer {
  public static void main(String[] args) {
    // This file will be compiled by script and must be at
    // the root of your project directory

    String confPath = "conf/httpd.conf";
    ConfReader confReader = new ConfReader(confPath);
  }
}
