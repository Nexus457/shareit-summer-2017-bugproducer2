package edu.hm.bugproducer.models;


public interface IBook {


    public String getAuthor();
    public String getTitle();
    public String getIsbn();

    public void setAuthor(String author);
    public void setTitle(String title);
    public void setIsbn(String isbn);

}
