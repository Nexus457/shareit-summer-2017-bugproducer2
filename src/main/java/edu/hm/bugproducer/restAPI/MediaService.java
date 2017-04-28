package edu.hm.bugproducer.restAPI;

import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.models.Disc;

import java.util.List;

/**
 * Created by Johannes Arzt on 25.04.17.
 */

public interface MediaService {

    MediaServiceResult addBook(Book book);

    MediaServiceResult addDisc(Disc disc);

    List<Book> getBooks();

    Book getBook(Book book);

    Disc getDisc(Disc disc);

    List<Disc> getDiscs();

    MediaServiceResult updateBook(Book book);

    MediaServiceResult updateDisc(Disc disc);

}
