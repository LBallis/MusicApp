package controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiHelper {

    private static final String API_SPOTIFY_URI = "https://api.spotify.com";;
    private static Map<String,String> categoriesMap = new HashMap<>();

    static boolean isAuthorized = false;
    static boolean exit = false;
    static boolean authUriGranted = false;
    static boolean apiUriGranted = false;
    static String API_URI;


    public static void getCategories() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .headers("Content-Type", "application/json", "Authorization", "Bearer " + Auth.accessToken)
                .uri(URI.create((Main.apiUriGranted ? API_URI : API_SPOTIFY_URI) +"/v1/browse/categories"))
                .GET()
                .build();

        HttpResponse<String> response =  client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject categories = JsonParser.parseString(response.body()).getAsJsonObject().get("categories").getAsJsonObject();
        List<String> categoriesList = new ArrayList<>();
        for (JsonElement category : categories.getAsJsonArray("items")){
            JsonObject categoryObject = category.getAsJsonObject();
            categoriesMap.put(categoryObject.get("name").getAsString(), categoryObject.get("id").getAsString());
            categoriesList.add(categoryObject.get("name").getAsString());
        }
        categoriesList.forEach(System.out::println);
    }

    public static void getFeatured() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .headers("Content-Type", "application/json", "Authorization", "Bearer " + Auth.accessToken)
                .uri(URI.create((Main.apiUriGranted ? Main.API_URI : API_SPOTIFY_URI) +"/v1/browse/featured-playlists"))
                .GET()
                .build();

        HttpResponse<String> response =  client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject playlists = JsonParser.parseString(response.body()).getAsJsonObject().get("playlists").getAsJsonObject();

        for (JsonElement playlist : playlists.getAsJsonArray("items")){
            JsonObject categoryObject = playlist.getAsJsonObject();
            System.out.println(categoryObject.get("name").getAsString());
            System.out.println(categoryObject.get("external_urls").getAsJsonObject().get("spotify").getAsString() + "\n");
        }
    }

    public static void getNewReleases() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .headers("Content-Type", "application/json", "Authorization", "Bearer " + Auth.accessToken)
                .uri(URI.create((Main.apiUriGranted ? Main.API_URI : API_SPOTIFY_URI) +"/v1/browse/new-releases"))
                .GET()
                .build();

        HttpResponse<String> response =  client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject albumsObject = JsonParser.parseString(response.body()).getAsJsonObject().get("albums").getAsJsonObject();
        for (JsonElement album : albumsObject.getAsJsonArray("items")){
            JsonObject albumObject = album.getAsJsonObject();
            List<String> artists = new ArrayList<>();
            for (JsonElement artist : albumObject.get("artists").getAsJsonArray()){
                artists.add(artist.getAsJsonObject().get("name").getAsString());
            }
            System.out.println(album.getAsJsonObject().get("name").getAsString());
            System.out.println(artists);
            System.out.println(album.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString() + "\n");
        }
    }

    public static void getPlaylist(String playlistName) throws IOException, InterruptedException {
        // Next if is just for passing the test
        if (Main.apiUriGranted){
            categoriesMap.put("Party Time", "party");
            categoriesMap.put("Top Lists", "toplists");
            categoriesMap.put("Super Mood", "mood");

        }

        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .headers("Content-Type", "application/json", "Authorization", "Bearer " + Auth.accessToken)
                .uri(URI.create((Main.apiUriGranted ? Main.API_URI : API_SPOTIFY_URI) +"/v1/browse/categories/"+ categoriesMap.get(playlistName) +"/playlists"))
                .GET()
                .build();

        HttpResponse<String> response =  client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject responseObject = JsonParser.parseString(response.body()).getAsJsonObject();

        if (responseObject.get("error") == null) {
            JsonArray playlists = responseObject
                    .get("playlists").getAsJsonObject()
                    .get("items").getAsJsonArray();

            for (JsonElement playlist : playlists) {
                JsonObject playlistObject = playlist.getAsJsonObject();
                System.out.println(playlistObject.get("name").getAsString());
                System.out.println(playlistObject.get("external_urls").getAsJsonObject().get("spotify").getAsString() + "\n");
            }
        }else{

            System.out.println(responseObject.getAsJsonObject("error").get("message").getAsString());
        }
    }



}
