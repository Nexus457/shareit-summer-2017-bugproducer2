package edu.hm.bugproducer.restAPI;

import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.models.Disc;
import edu.hm.bugproducer.models.Medium;

import java.util.List;

/**
 * Created by Johannes Arzt on 25.04.17.
 */
public interface MediaService {
    public MediaServiceResult addBook(Book book);

    public MediaServiceResult addDisc(Disc disc);

    public List<Book> getBooks();

    public List<Disc> getDiscs();

    public MediaServiceResult updateBook(Book book);

    public MediaServiceResult updateDisc(Disc disc);

}
