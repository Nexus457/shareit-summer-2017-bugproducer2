package edu.hm.bugproducer.models;

public class Disc extends Medium{

    private String barcode;
    private String director;
    private String fsk;

    public Disc(String director, String barcode, String title, String fsk) {
        super(title);
        this.barcode = barcode;
        this.director = director;
        this.fsk = fsk;
    }

    public Disc() {
        super("");
    }

    public String getBarcode() {
        return barcode;
    }

    public String getDirector() {
        return director;
    }

    public String getFsk() {
        return fsk;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
