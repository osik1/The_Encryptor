package org.example.api;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.symmetric.AES;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.json.JSONObject;

public class AESEncrypt implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                // Read the request body
                InputStream inputStream = exchange.getRequestBody();
                byte[] requestBodyBytes = inputStream.readAllBytes();
                String requestBody = new String(requestBodyBytes, StandardCharsets.UTF_8);

                String key = RandomUtil.randomString(16);

                JSONObject jsonRequest = new JSONObject(requestBody);
                String content = jsonRequest.getString("content");

                AES aes = new AES("ECB", "PKCS5Padding", key.getBytes(StandardCharsets.UTF_8));

                // Encrypt the content
                byte[] encryptedBytes = aes.encrypt(content.getBytes(StandardCharsets.UTF_8));

                // Base64 encode the encrypted bytes
                String encryptedContent = Base64.getEncoder().encodeToString(encryptedBytes);

                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(200, encryptedContent.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(encryptedContent.getBytes(StandardCharsets.UTF_8));
                }
            } catch (org.json.JSONException e) {
                sendErrorResponse(exchange, 400, "Invalid JSON format. Expected: {\"content\": \"text to encrypt\"");
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, "Encryption failed: " + e.getMessage());
            }
        } else {
            sendErrorResponse(exchange, 405, "Method Not Allowed. Use POST");
        }
    }

    /**
     * Send error response with specified status code and message
     */
    private void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(statusCode, message.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(message.getBytes(StandardCharsets.UTF_8));
        }
    }
}