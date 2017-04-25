package edu.hm.bugproducer.restAPI;

import edu.hm.bugproducer.Utils.Isbn;
import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.models.Disc;
import edu.hm.bugproducer.models.Medium;

import java.util.ArrayList;
import java.util.List;

import static edu.hm.bugproducer.restAPI.MediaServiceResult.*;

public class MediaServiceImpl implements MediaService {


    private static List<Book> books = new ArrayList<>();
    private static List<Disc> discs = new ArrayList<>();

    @Override
    public MediaServiceResult addBook(Book book) {
        MediaServiceResult mediaServiceResult;

        if (book == null) {
            mediaServiceResult = MSR_NO_CONTENT;
        } else if (book.getAuthor().isEmpty() || book.getTitle().isEmpty() || book.getIsbn().isEmpty()) {
            mediaServiceResult = MSR_BAD_REQUEST;
        } else if (Isbn.isValid(book.getIsbn())) {
            mediaServiceResult = MSR_BAD_REQUEST;
        } else {
            mediaServiceResult = MSR_OK;
            books.add(book);
        }
        return mediaServiceResult;
    }

    @Override
    public MediaServiceResult addDisc(Disc disc) {
        MediaServiceResult result;
        if (disc == null) {
            result = MSR_BAD_REQUEST;
        }
        //ToDo FSK fehlt noch!
        else if (disc.getBarcode().isEmpty() || disc.getDirector().isEmpty() || disc.getTitle().isEmpty()) {
            result = MSR_NO_CONTENT;

        } else {
            result = MSR_OK;
            discs.add(disc);

        }
        return result;
    }


    @Override
    public List<Book> getBooks() {
        return books;
    }

    @Override
    public List<Disc> getDiscs() {
        return discs;
    }

    @Override
    public MediaServiceResult updateBook(Book book) {



        return null;
    }

    @Override
    public MediaServiceResult updateDisc(Disc disc) {
        return null;
    }
}
