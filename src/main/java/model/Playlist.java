package model;

public class Playlist extends Result{
    private String uri;

    public Playlist(String name, String uri) {
        super(name);
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return this.getName() +"\n" +
                uri + "\n";
    }
}
