package edu.hm.bugproducer.models;

/**
 * Created by mark on 05.06.2017.
 */
public interface IDisc {

    public String getTitle();
    public String getBarcode();
    public String getDirector();
    public int getFsk();

    public void setTitle(String title);
    public void setBarcode(String barcode);
    public void setDirector(String director);
    public void setFsk(int fsk);


}
