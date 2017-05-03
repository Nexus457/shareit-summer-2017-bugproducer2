package edu.hm.bugproducer.restAPI.media;

import edu.hm.bugproducer.Utils.Isbn;
import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.models.Disc;
import edu.hm.bugproducer.restAPI.MediaServiceResult;
import javafx.util.Pair;
import org.apache.commons.validator.routines.checkdigit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

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
        } else if (!EAN13CheckDigit.EAN13_CHECK_DIGIT.isValid(disc.getBarcode())) {
            mediaServiceResult = MSR_BAD_REQUEST;
        } else if (disc.getBarcode().isEmpty() || disc.getDirector().isEmpty() || disc.getTitle().isEmpty() || disc.getFsk() < 0) {
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
        Pair<MediaServiceResult, Book> myResult = null;

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
    public Pair<MediaServiceResult, Disc> getDisc(String barcode) {
        Pair<MediaServiceResult, Disc> myResult = null;

        for (Disc d : discs) {
            if (d.getBarcode().equals(barcode)) {
                myResult = new Pair<>(MSR_OK, d);
            } else {
                myResult = new Pair<>(MSR_NOT_FOUND, null);
            }
        }
        return myResult;
    }

    @Override
    public List<Disc> getDiscs() {
        return discs;
    }

    //todo Was passiert bei ungültiger ISBN
    @Override
    public MediaServiceResult updateBook(String isbn, Book newBook) {
        MediaServiceResult mediaServiceResult = MSR_INTERNAL_SERVER_ERROR;

        List<Book> oneBook = getBooks().stream().filter(b ->
                b.getIsbn().equals(isbn)
        ).collect(Collectors.toList());
        boolean validIsbn = true;
        if (!oneBook.isEmpty()) {
            if (newBook.getIsbn() != null) {
                 validIsbn = Isbn.isValid(newBook.getIsbn());
                if (!validIsbn) {
                    mediaServiceResult = MSR_BAD_REQUEST;
                } else {
                    for (Book b : oneBook) {
                        b.setIsbn(newBook.getIsbn());
                    }
                    mediaServiceResult = MSR_OK;
                }
            }
            if (newBook.getTitle() != null && validIsbn) {
                for (Book b : oneBook) {
                    b.setTitle(newBook.getTitle());
                }
                mediaServiceResult = MSR_OK;
            }

            if (newBook.getAuthor() != null && validIsbn) {
                for (Book b : oneBook) {
                    b.setAuthor(newBook.getAuthor());
                }
                mediaServiceResult = MSR_OK;
            }
        } else {
            mediaServiceResult = MSR_BAD_REQUEST;
        }

        return mediaServiceResult;
    }

    @Override
    public MediaServiceResult updateDisc(String barcode, Disc newDisc) {
        MediaServiceResult mediaServiceResult = MSR_INTERNAL_SERVER_ERROR;

        List<Disc> oneDisc = getDiscs().stream().filter(d ->
                d.getBarcode().equals(barcode)
        ).collect(Collectors.toList());

        boolean validEAN = true;

        if (!oneDisc.isEmpty()) {

            if (newDisc.getBarcode() != null) {
             validEAN = EAN13CheckDigit.EAN13_CHECK_DIGIT.isValid(newDisc.getBarcode());
                if (!validEAN) {
                    mediaServiceResult = MSR_BAD_REQUEST;
                } else {
                    for (Disc d : oneDisc) {
                        d.setBarcode(newDisc.getBarcode());
                    }
                    mediaServiceResult = MSR_OK;
                }
            }

            if (newDisc.getDirector() != null && validEAN) {
                for (Disc d : oneDisc) {
                    d.setDirector(newDisc.getDirector());
                }
                mediaServiceResult = MSR_OK;
            }
            if (newDisc.getFsk() != 0 && validEAN) {
                for (Disc d : oneDisc) {
                    d.setFsk(newDisc.getFsk());
                }
                mediaServiceResult = MSR_OK;
            }
            if (!newDisc.getTitle().isEmpty() && validEAN) {
                for (Disc d : oneDisc) {
                    d.setTitle(newDisc.getTitle());
                }
                mediaServiceResult = MSR_OK;
            }
        } else {
            mediaServiceResult = MSR_BAD_REQUEST;
        }

        return mediaServiceResult;
    }
}
