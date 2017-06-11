package edu.hm.bugproducer.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Disc Class.
 * @author Mark Tripolt
 * @author Johannes Arzt
 * @author Tom Maier
 * @author Patrick Kuntz
 */
@Entity
@Table(name = "TDisc")
public class Disc extends Medium implements Serializable{
    /** unique number of a disc */
    @Id
    private String barcode;
    /** name of the director of the disc*/
    @Column
    private String director;
    /** required age to watch the disc */
    @Column
    private int fsk;

    /**
     * Disc Constructor with parameters.
     * @param director who created the movie
     * @param barcode unique number of a disc
     * @param title name of a disc
     * @param fsk required age to watch the disc
     */
    public Disc(String director, String barcode, String title, int fsk) {
        super(title);
        this.barcode = barcode;
        this.director = director;
        this.fsk = fsk;
    }

    /**
     * Disc Constructor.
     */
    public Disc() {
        super("");
    }

    /**
     * getBarcode method.
     * gets the barcode of a disc
     * @return barcode
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * getDirector method.
     * gets the director of a disc
     * @return director
     */
    public String getDirector() {
        return director;
    }

    /**
     * getFsk method.
     * gets the fsk of a disc
     * @return fsk
     */
    public int getFsk() {
        return fsk;
    }

    /**
     * setBarcode method.
     * sets the barcode for a disc
     * @param barcode unique number
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * setDirector method.
     * sets the diretctor for a disc
     * @param director name of creator
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * setFsk method.
     * sets the required age for a disc
     * @param fsk required age
     */
    public void setFsk(int fsk) {
        this.fsk = fsk;
    }

    public void setTitle(String title){
        super.setTitle(title);
    }

    public String getTitle(){
        return super.getTitle();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Disc)) return false;
        if (!super.equals(o)) return false;

        Disc disc = (Disc) o;

        if (getFsk() != disc.getFsk()) return false;
        if (!getBarcode().equals(disc.getBarcode())) return false;
        return getDirector().equals(disc.getDirector());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getBarcode().hashCode();
        result = 31 * result + getDirector().hashCode();
        result = 31 * result + getFsk();
        return result;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
