package ResponseRequest;

import java.util.HashMap;

public class HTTPMessage {

    public static class StartLine {
        public String version;

        public StartLine(String version) {
            this.version = version;
        }
    }
    public static class ResponseStartLine extends StartLine{
        public ResponseCode responseCode;

        public ResponseStartLine(ResponseCode responseCode, String version) {
            super(version);
            this.responseCode = responseCode;
        }
    }

    public static class RequestStartLine extends StartLine{
        public HTTPMethod method;
        public String target;

        public RequestStartLine(HTTPMethod method, String target, String version) {
            super(version);
            this.method = method;
            this.target = target;
        }
    }

    protected StartLine startLine;
    protected HashMap<String, String> headers;
    protected String body;

    public HTTPMessage() {
        this.headers = new HashMap<>();
    }

    public HTTPMessage(StartLine startLine, HashMap<String, String> headers, String body) {
        this.startLine = startLine;
        this.headers = headers;
        this.body = body;
    }

    public StartLine getStartLine() {
        return startLine;
    }

    public HTTPMessage setStartLine(StartLine startLine) {
        this.startLine = startLine;
        return this;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public HTTPMessage setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public HTTPMessage addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public String getBody() {
        return body;
    }

    public HTTPMessage setBody(String body) {
        this.body = body;
        return this;
    }

    @Override
    public String toString() {
        return "HTTPMessage{" +
                "startLine=" + startLine +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
