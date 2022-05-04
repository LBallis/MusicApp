package controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Auth {

    private static final String CLIENT_ID = "322cc29c5b19484f999e94ec201c0866";
    private static final String CLIENT_SECRET = "59d2f56563b54b9b80927f0ef87e8fa3";
    private static final String AUTH_SPOTIFY_URI = "https://accounts.spotify.com";
    private static final String REDIRECT_URI = "http://localhost:8080/";

    static String authCode = "";
    static String accessToken = "";

    public static void init() throws IOException {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);
        server.createContext("/",
                new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        String query = "";
                        final String SUCCESS = "Got the code. Return back to your program.";
                        final String FAILURE = "Authorization code not found. Try again.";

                        query += exchange.getRequestURI().getQuery();
                        String response  = FAILURE;

                        if (query.contains("code")) {
                            response = SUCCESS;
                            System.out.println("code received");
                        }
                        exchange.sendResponseHeaders(200, response.length());
                        exchange.getResponseBody().write(response.getBytes());
                        exchange.getResponseBody().close();

                        authCode = query.substring(5);
                    }
                }
        );

        server.start();

        System.out.println("use this link to request the access code:");
        System.out.println((Main.authUriGranted ? Main.AUTH_URI : AUTH_SPOTIFY_URI) +"/authorize"+"?client_id="+ CLIENT_ID +"&redirect_uri="+ REDIRECT_URI +"&response_type=code");

        System.out.println("waiting for code...");
        while (authCode.isEmpty()){
            Thread.onSpinWait();
        }

        server.stop(1);
    }

    public static boolean getAccessToken() throws IOException, InterruptedException {
        System.out.println("making http request for access_token...");
        HttpClient client = HttpClient.newBuilder().build();

        Map<Object, Object> data = new HashMap<>();
        data.put("grant_type", "authorization_code");
        data.put("code", authCode);
        data.put("redirect_uri", "http://localhost:8080/");


        HttpRequest request = HttpRequest.newBuilder()
                .headers("Content-Type", "application/x-www-form-urlencoded", "Authorization", "Basic " + Base64.getEncoder().encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes()))
                .uri(URI.create((Main.authUriGranted ? Main.AUTH_URI : AUTH_SPOTIFY_URI) +"/api/token"))
                .POST(buildFormDataFromMap(data))
                .build();

        HttpResponse<String> response =  client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.body().contains("access_token")){
            JsonObject jsonObj = (JsonObject) JsonParser.parseString(response.body());
            accessToken += jsonObj.get("access_token").getAsString();
        }
        System.out.println(accessToken);
        return response.body().contains("access_token");
    }

    private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

}
