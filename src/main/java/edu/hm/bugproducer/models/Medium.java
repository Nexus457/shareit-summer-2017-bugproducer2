package edu.hm.bugproducer.models;

public abstract class Medium {
    private String title;

    public Medium(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
