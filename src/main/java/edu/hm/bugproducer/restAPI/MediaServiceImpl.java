package edu.hm.bugproducer.restAPI;

import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.models.Disc;
import edu.hm.bugproducer.models.Medium;

import java.util.ArrayList;
import java.util.List;

import static edu.hm.bugproducer.restAPI.MediaServiceResult.*;

public class MediaServiceImpl implements MediaService {


    private static List<Book> books = new ArrayList<>();

    @Override
    public MediaServiceResult addBook(Book book) {
        MediaServiceResult result;

        if (book == null) {
            result = MSR_NO_CONTENT;
        } else if (book.getAuthor().isEmpty() || book.getTitle().isEmpty() || book.getIsbn().isEmpty()) {
            result = MSR_BAD_REQUEST;
        } else {
            result = MSR_OK;
            books.add(book);
        }
        return result;
    }

    @Override
    public MediaServiceResult addDisc(Disc disc) {
        return null;
    }

    @Override
    public Medium[] getBooks() {
        return new Medium[0];
    }

    @Override
    public Medium[] getDisc() {
        return new Medium[0];
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
