package edu.hm.bugproducer.restAPI;

import edu.hm.bugproducer.Utils.Isbn;
import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.models.Disc;
import org.apache.commons.validator.routines.checkdigit.*;

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
        } else if (!Isbn.isValid(book.getIsbn())) {
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
        else if (!EAN13CheckDigit.EAN13_CHECK_DIGIT.isValid(disc.getBarcode())){
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
        MediaServiceResult mediaServiceResult = MSR_INTERNAL_SERVER_ERROR;

        if (book == null) {
            mediaServiceResult = MSR_BAD_REQUEST;
        }

        if (book.getTitle().isEmpty() || book.getAuthor().isEmpty()) {
            mediaServiceResult = MSR_NO_CONTENT;
        } else {
            for (Book b : getBooks()) {
                if (!book.getIsbn().equals(b.getIsbn())) {
                    // isbn not found
                    mediaServiceResult = MSR_BAD_REQUEST;
                } else {
                    // replace object
                    books.remove(b);
                    books.add(book);
                    mediaServiceResult = MSR_OK;
                }
            }
        }

        return mediaServiceResult;
    }

    @Override
    public MediaServiceResult updateDisc(Disc disc) {
        MediaServiceResult mediaServiceResult = MSR_INTERNAL_SERVER_ERROR;

        if (disc == null) {
            mediaServiceResult = MSR_BAD_REQUEST;
        }

        // ToDO add fsk check
        if (disc.getTitle().isEmpty() || disc.getDirector().isEmpty()) {
            mediaServiceResult = MSR_NO_CONTENT;
        } else {
            for (Disc d : getDiscs()) {
                if (!disc.getBarcode().equals(d.getBarcode())) {
                    // barcode not found
                    mediaServiceResult = MSR_BAD_REQUEST;
                } else {
                    // replace object
                    discs.remove(d);
                    discs.add(disc);
                    mediaServiceResult = MSR_OK;
                }
            }
        }

        return mediaServiceResult;
    }
}
