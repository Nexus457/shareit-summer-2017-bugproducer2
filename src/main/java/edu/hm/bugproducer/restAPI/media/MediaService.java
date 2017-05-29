package edu.hm.bugproducer.restAPI.media;

import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.models.Disc;
import edu.hm.bugproducer.restAPI.MediaServiceResult;
import javafx.util.Pair;

import java.util.List;

/**
 * Created by Johannes Arzt on 25.04.17.
 */

public interface MediaService {

    /**
     * addBook method.
     * checks if a book contains essential information and if the isbn is valid to add it to the list books
     *
     * @param book book object
     * @return mediaServiceResult
     */
    MediaServiceResult addBook(Book book);

    /**
     * addDisc method.
     * checks if a disc contains essential information and if the barcode is valid to add it to the list discs
     *
     * @param disc disc object
     * @return mediaServiceResult
     */
    MediaServiceResult addDisc(Disc disc);
    /**
     * getBooks method.
     * getter for the books list
     *
     * @return books a list of books
     */
    List<Book> getBooks();

    /**
     * getBook method.
     * gets a specific book by his isbn
     *
     * @param isbn unique number of book
     * @return myResult pair of statusCode and the book object or null
     */
    Pair<MediaServiceResult, Book> getBook(String isbn);

    /**
     * getDisc method.
     * gets a specific disc by his barcode
     *
     * @param barcode unique number of disc
     * @return myResult pair of statusCode and disc object or null
     */
    Pair<MediaServiceResult, Disc> getDisc(String barcode);
    /**
     * getDiscs method.
     * getter for list of discs
     *
     * @return discs
     */
    List<Disc> getDiscs();
    /**
     * updateBook method.
     * checking if the information of the new book are okay, and updates the book
     *
     * @param isbn    unique number of a book
     * @param book newBook object
     * @return statusCode
     */
    MediaServiceResult updateBook(String isbn, Book book);

    /**
     * updateDisc method.
     * checking if the information of the new disc are okay, and updates the disc
     *
     * @param barcode unique number of a disc
     * @param disc newDisc object
     * @return statusCode
     */
    MediaServiceResult updateDisc(String barcode, Disc disc);

    /**
     * deleteAll method.
     * deletes books and discs list
     * @return mediaServiceResult
     */
    MediaServiceResult deleteAll();

}
