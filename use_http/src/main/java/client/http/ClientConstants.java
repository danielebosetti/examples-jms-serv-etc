package client.http;

public class ClientConstants {
    public static final String HTTP_GET_REQ_1 = "GET /docs/ HTTP/1.1";
    
    public static final String HTTP_SERVER_HOST = "localhost";
    public static final int HTTP_PORT = 8199;
    
    public static final String[] HTTP_REQ_HEADER = new String[] {
            "Host: localhost",
            "Accept: text/html,*/*",
            "Accept-Language: en-us",
            "Accept-Encoding: gzip, deflate",
            "User-Agent: Mozilla/5.0 (Windows NT 10.0;) Gecko/20100101 Firefox/63.0"
        };
        

}
