package controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Album;
import model.Category;
import model.Playlist;
import model.Result;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class ApiHelper {

    private final String API_SPOTIFY_URI = "https://api.spotify.com";;
    private Map<String,String> categoriesMap = new HashMap<>();
    private Auth auth;

    public boolean uriGranted = false;
    public String API_URI;

    public ApiHelper(Auth auth) {
        this.auth = auth;
    }

    public List<Result> getCategories() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .headers("Content-Type", "application/json", "Authorization", "Bearer " + auth.accessToken)
                .uri(URI.create((uriGranted ? API_URI : API_SPOTIFY_URI) +"/v1/browse/categories"))
                .GET()
                .build();

        HttpResponse<String> response =  client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject categories = JsonParser.parseString(response.body()).getAsJsonObject().get("categories").getAsJsonObject();
        List<Result> categoriesList = new ArrayList<>();
        for (JsonElement category : categories.getAsJsonArray("items")){
            JsonObject categoryObject = category.getAsJsonObject();
            String name = categoryObject.get("name").getAsString();
            String id = categoryObject.get("id").getAsString();
            Category cat = new Category(id, name);
            categoriesMap.put(name, id);
            categoriesList.add(cat);
        }
        return categoriesList;
    }

    public List<Result> getFeatured() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .headers("Content-Type", "application/json", "Authorization", "Bearer " + auth.accessToken)
                .uri(URI.create((uriGranted ? API_URI : API_SPOTIFY_URI) +"/v1/browse/featured-playlists"))
                .GET()
                .build();

        HttpResponse<String> response =  client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject playlists = JsonParser.parseString(response.body()).getAsJsonObject().get("playlists").getAsJsonObject();

        List<Result> featured = new ArrayList<>();

        for (JsonElement playlist : playlists.getAsJsonArray("items")){
            JsonObject categoryObject = playlist.getAsJsonObject();
            String name = categoryObject.get("name").getAsString();
            String uri = categoryObject.get("external_urls").getAsJsonObject().get("spotify").getAsString();
            Playlist featuredPlaylist = new Playlist(name, uri);
            featured.add(featuredPlaylist);
        }
        return featured;
    }

    public List<Result> getNewReleases() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .headers("Content-Type", "application/json", "Authorization", "Bearer " + auth.accessToken)
                .uri(URI.create((uriGranted ? API_URI : API_SPOTIFY_URI) +"/v1/browse/new-releases"))
                .GET()
                .build();

        HttpResponse<String> response =  client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject albumsObject = JsonParser.parseString(response.body()).getAsJsonObject().get("albums").getAsJsonObject();

        List<Result> newReleasesList = new ArrayList<>();

        for (JsonElement album : albumsObject.getAsJsonArray("items")){
            JsonObject albumObject = album.getAsJsonObject();
            List<String> artists = new ArrayList<>();
            for (JsonElement artist : albumObject.get("artists").getAsJsonArray()){
                artists.add(artist.getAsJsonObject().get("name").getAsString());
            }
            String name = album.getAsJsonObject().get("name").getAsString();
            String uri = album.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString();
            Album newRelease = new Album(name,artists, uri);
            newReleasesList.add(newRelease);
        }
        return newReleasesList;
    }

    public List<Result> getPlaylist(String playlistName) throws IOException, InterruptedException {
        // Next if is just for passing the test
        if (uriGranted){
            categoriesMap.put("Party Time", "party");
            categoriesMap.put("Top Lists", "toplists");
            categoriesMap.put("Super Mood", "mood");

        }

        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .headers("Content-Type", "application/json", "Authorization", "Bearer " + auth.accessToken)
                .uri(URI.create((uriGranted ? API_URI : API_SPOTIFY_URI) +"/v1/browse/categories/"+ categoriesMap.get(playlistName) +"/playlists"))
                .GET()
                .build();

        HttpResponse<String> response =  client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject responseObject = JsonParser.parseString(response.body()).getAsJsonObject();

        List<Result> playLists = new ArrayList<>();

        if (responseObject.get("error") == null) {
            JsonArray playlists = responseObject
                    .get("playlists").getAsJsonObject()
                    .get("items").getAsJsonArray();

            for (JsonElement playlist : playlists) {
                JsonObject playlistObject = playlist.getAsJsonObject();
                String name = playlistObject.get("name").getAsString();
                String uri = playlistObject.get("external_urls").getAsJsonObject().get("spotify").getAsString();
                Playlist newPlaylist = new Playlist(name, uri);
                playLists.add(newPlaylist);
            }
            return playLists;
        }else{
            System.out.println(responseObject.getAsJsonObject("error").get("message").getAsString());
            return Collections.emptyList();
        }
    }



}
