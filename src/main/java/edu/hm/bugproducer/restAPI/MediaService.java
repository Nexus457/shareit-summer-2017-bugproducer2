package edu.hm.bugproducer.restAPI;

import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.models.Disc;
import javafx.util.Pair;

import java.util.List;

/**
 * Created by Johannes Arzt on 25.04.17.
 */

public interface MediaService {

    MediaServiceResult addBook(Book book);

    MediaServiceResult addDisc(Disc disc);

    List<Book> getBooks();

    Pair<MediaServiceResult, Book> getBook(String isbn);

    Pair<MediaServiceResult, Disc> getDisc(String barcode);

    List<Disc> getDiscs();

    MediaServiceResult updateBook(Book book);

    MediaServiceResult updateDisc(Disc disc);

}
