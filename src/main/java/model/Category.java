package model;

public class Category extends Result{
    private String id;

    public Category(String id, String name) {
        super(name);
        this.id = id;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
