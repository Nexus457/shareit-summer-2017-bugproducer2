package edu.hm.bugproducer.models;

public class Disc extends Medium{

    private String barcode;
    private String director;
    private int fsk;

    public Disc(String barcode, String director, int fsk, String title) {
        super(title);
        this.barcode = barcode;
        this.director = director;
        this.fsk = fsk;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getDirector() {
        return director;
    }

    public int getFsk() {
        return fsk;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
