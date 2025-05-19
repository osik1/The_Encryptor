package org.example.api;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;



public class helloWorld implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "Hello World I'm building encryptor";

        exchange.getResponseHeaders().set("Content-Type", "json");
        exchange.sendResponseHeaders(200, response.length());

        try(OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
