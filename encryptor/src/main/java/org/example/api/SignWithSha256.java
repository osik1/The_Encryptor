package org.example.api;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

public class SignWithSha256 implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            // Reading the request
            InputStream inputStream = exchange.getRequestBody();
            byte[] requestBodyBytes = inputStream.readAllBytes();
            String content = new String(requestBodyBytes, StandardCharsets.UTF_8);

            // Creating SHA-256 hash
            Digester sha256 = new Digester(DigestAlgorithm.SHA256);
            String signedContent = sha256.digestHex(content);

            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, signedContent.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(signedContent.getBytes(StandardCharsets.UTF_8));
            }
        }else {
            String response = "Method Not Allowed. Use POST.";
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(405, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        }

    }
}
