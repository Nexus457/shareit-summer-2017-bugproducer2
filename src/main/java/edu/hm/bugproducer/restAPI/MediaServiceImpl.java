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
