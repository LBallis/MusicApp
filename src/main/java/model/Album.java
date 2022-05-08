package model;

import java.util.List;

public class Album extends Result{
    private List<String> artists;
    private String uri;

    public Album(String name, List<String> artists, String uri) {
        super(name);
        this.artists = artists;
        this.uri = uri;
    }

    public List<String> getArtists() {
        return artists;
    }

    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return this.getName() + "\n" +
                artists.toString() + "\n" +
                uri + "\n";
    }
}
