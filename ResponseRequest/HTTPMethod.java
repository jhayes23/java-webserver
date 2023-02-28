package ResponseRequest;

public enum HTTPMethod {
    GET,
    HEAD,
    POST,
    PUT,
    DELETE;

    public static HTTPMethod stringToHTTPMethod(String method) {
        switch(method.toUpperCase()) {
            case "GET":
                return GET;
            case "HEAD":
                return HEAD;
            case "POST":
                return POST;
            case "PUT":
                return PUT;
            case "DELETE":
                return DELETE;
        }
        return null;
    }
}
