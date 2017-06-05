package edu.hm.bugproducer.models;

/**
 * Book Class.
 * @author Mark Tripolt
 * @author Johannes Arzt
 * @author Tom Maier
 * @author Patrick Kuntz
 */
public class Book extends Medium implements IBook  {
    /** author of a book  */
    private String author;
    /** unique isbn of a book */
    private String isbn;

    /**
     * Book Constructor.
     * used for books without a title
     */
    public Book() {
        super("No Name");
    }

    /**
     * Book Constructor.
     * @param author name of the writer
     * @param isbn unique identification number
     * @param title name of book
     */
    public Book(String author, String isbn, String title) {
        super(title);
        this.author = author;
        this.isbn = isbn;
    }

    /**
     * getAuthor method.
     * gives you the name of the author of a book
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * setAuthor method.
     * you can set the name of an author
     * @param author string variable
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * getIsbn method.
     * gives you the isbn of a book
     *
     * @return isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * setIsbn method.
     * set the isbn you want
     * @param isbn string variable
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn.replaceAll("-", "");
    }

    public String getTitle(){
        return super.getTitle();
    }

    public void setTitle(String title) {
        super.setTitle(title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;

        Book book = (Book) o;

        if (!getAuthor().equals(book.getAuthor())) return false;
        return getIsbn().equals(book.getIsbn());
    }

    @Override
    public int hashCode() {
        int result = getAuthor().hashCode();
        result = 31 * result + getIsbn().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}
