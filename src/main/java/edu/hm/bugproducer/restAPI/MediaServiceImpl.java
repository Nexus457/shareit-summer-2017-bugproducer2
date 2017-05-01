package edu.hm.bugproducer.restAPI;

import edu.hm.bugproducer.Utils.Isbn;
import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.models.Disc;
import javafx.util.Pair;
import org.apache.commons.validator.routines.checkdigit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static edu.hm.bugproducer.restAPI.MediaServiceResult.*;

public class MediaServiceImpl implements MediaService {


    public static List<Book> books = new ArrayList<>();
    public static List<Disc> discs = new ArrayList<>();

    @Override
    public MediaServiceResult addBook(Book book) {
        MediaServiceResult mediaServiceResult = MSR_INTERNAL_SERVER_ERROR;

        if (book == null) {
            mediaServiceResult = MSR_NO_CONTENT;
        } else if (book.getAuthor().isEmpty() || book.getTitle().isEmpty() || book.getIsbn().isEmpty()) {
            mediaServiceResult = MSR_BAD_REQUEST;
        } else if (!Isbn.isValid(book.getIsbn())) {
            mediaServiceResult = MSR_BAD_REQUEST;
        } else {
            if (books.isEmpty()) {
                mediaServiceResult = MSR_OK;
                books.add(book);
            } else {

                ListIterator<Book> lir = books.listIterator();

                while (lir.hasNext()) {
                    if (lir.next().getIsbn().equals(book.getIsbn())) {
                        mediaServiceResult = MSR_BAD_REQUEST;
                    } else {
                        lir.add(book);
                        mediaServiceResult = MSR_OK;
                    }
                }
            }
        }

        return mediaServiceResult;
    }

    @Override
    public MediaServiceResult addDisc(Disc disc) {
        MediaServiceResult mediaServiceResult = MSR_INTERNAL_SERVER_ERROR;
        System.out.println(disc.getFsk());

        if (disc == null) {
            mediaServiceResult = MSR_NO_CONTENT;
            System.err.println("NULL");
        } else if (!EAN13CheckDigit.EAN13_CHECK_DIGIT.isValid(disc.getBarcode())) {
            System.err.println("BARCODE");
            mediaServiceResult = MSR_BAD_REQUEST;
        }
        //ToDo FSK fehlt noch!

        else if (disc.getBarcode().isEmpty() || disc.getDirector().isEmpty() || disc.getTitle().isEmpty()||disc.getFsk()<0) {
            mediaServiceResult = MSR_NO_CONTENT;
        } else {
            if (discs.isEmpty()) {
                mediaServiceResult = MSR_OK;
                discs.add(disc);
            } else {

                ListIterator<Disc> lir = discs.listIterator();

                while (lir.hasNext()) {
                    if (lir.next().getBarcode().equals(disc.getBarcode())) {
                        mediaServiceResult = MSR_BAD_REQUEST;
                    } else {
                        lir.add(disc);
                        mediaServiceResult = MSR_OK;
                    }
                }
            }

        }


        return mediaServiceResult;
    }


    @Override
    public List<Book> getBooks() {
        return books;
    }

    @Override
    public Pair<MediaServiceResult, Book> getBook(String isbn) {
        Pair<MediaServiceResult,Book> myResult = null;

        for (Book b : books) {
            if (b.getIsbn().equals(isbn)) {
                myResult = new Pair<>(MSR_OK, b);
            } else {
                myResult = new Pair<>(MSR_NOT_FOUND, null);
            }
        }
        return myResult;
    }

    @Override
    public Disc getDisc(String barcode) {
        Disc resultDisc = null;
        for (Disc d : discs) {

            if (d.getBarcode().equals(barcode)) {
                resultDisc = d;
            } else {
                resultDisc = null;
            }
        }
        return resultDisc;
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
