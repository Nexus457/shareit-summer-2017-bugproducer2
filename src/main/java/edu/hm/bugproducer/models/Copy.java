package edu.hm.bugproducer.models;

public class Copy {
    private Medium medium;
    private User owner;


    public Copy() {
    }

    public Copy(Medium medium, User owner) {
        this.medium = medium;
        this.owner = owner;
    }

    public Medium getMedium() {
        return medium;
    }

    public User getOwner() {
        return owner;
    }
}
