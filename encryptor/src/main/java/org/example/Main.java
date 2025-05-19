package org.example;
import com.sun.net.httpserver.HttpServer;
import org.example.api.AESEncrypt;
import org.example.api.SignWithSha256;
import org.example.api.helloWorld;
import java.net.InetSocketAddress;



public class Main{
    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Endpoints
        server.createContext("/api/hello", new helloWorld());
        server.createContext("/api/sign/content", new SignWithSha256());
        server.createContext("/api/aes/encrypt", new AESEncrypt());

        server.setExecutor(null);

        server.start();
        System.out.println("Server started on port 8080");
    }
}