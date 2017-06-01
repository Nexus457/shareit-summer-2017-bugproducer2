package edu.hm.bugproducer.restAPI.media;

import edu.hm.bugproducer.Status.StatusMgnt;
import edu.hm.bugproducer.Utils.Isbn;
import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.models.Disc;
import edu.hm.bugproducer.Status.MediaServiceResult;
import javafx.util.Pair;
import org.apache.commons.validator.routines.checkdigit.EAN13CheckDigit;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import static edu.hm.bugproducer.Status.MediaServiceResult.*;

/**
 * MediaServiceImpl Class.
 *
 * @author Mark Tripolt
 * @author Johannes Arzt
 * @author Tom Maier
 * @author Patrick Kuntz
 */
public class MediaServiceImpl implements MediaService {
    /**
     * ArrayList that contains the books.
     */
    public static List<Book> books = new ArrayList<>();
    /**
     * ArrayList that contains the discs.
     */
    public static List<Disc> discs = new ArrayList<>();


    @Override
    public StatusMgnt addBook(Book book) {
        StatusMgnt status = new StatusMgnt(MSR_INTERNAL_SERVER_ERROR, "An internal error has occurred");


        if (book == null) {
             status = new StatusMgnt(MSR_NO_CONTENT, "The book was empty");
        } else if (book.getAuthor().isEmpty() || book.getTitle().isEmpty() || book.getIsbn().isEmpty()) {
            status = new StatusMgnt(MSR_BAD_REQUEST, "Author or title or ISBN was empty");
        } else if (!Isbn.isValid(book.getIsbn())) {
            status = new StatusMgnt(MSR_BAD_REQUEST, "ISBN was not valid");
        } else {
            if (books.isEmpty()) {
                status = new StatusMgnt(MSR_OK, "ok");
                books.add(book);
            } else {

                boolean duplicate = false;

                ListIterator<Book> lir = books.listIterator();


                while (lir.hasNext() && !duplicate) {
                    if (lir.next().getIsbn().equals(book.getIsbn())) {
                        duplicate = true;
                        status = new StatusMgnt(MSR_BAD_REQUEST, "The book is already in the system. No duplicate allowed");
                    } else {
                        status = new StatusMgnt(MSR_OK, "ok");
                    }
                }

                if (!duplicate) {
                    books.add(book);
                }
            }
        }

        return status;
    }


    @Override
    public StatusMgnt addDisc(Disc disc) {
        StatusMgnt status = new StatusMgnt(MSR_INTERNAL_SERVER_ERROR, "An internal error has occurred");
        System.out.println(disc.getFsk());

        if (disc == null) {
            status = new StatusMgnt(MSR_NO_CONTENT, "The disc was empty");
        } else if (!EAN13CheckDigit.EAN13_CHECK_DIGIT.isValid(disc.getBarcode())) {
            status = new StatusMgnt(MSR_BAD_REQUEST, "Barcode was not valid");
        } else if (disc.getBarcode().isEmpty() || disc.getDirector().isEmpty() || disc.getTitle().isEmpty() || disc.getFsk() < 0) {
            status = new StatusMgnt(MSR_BAD_REQUEST, "Barcode or director or title was empty or FSK was less than 0 ");
        } else {
            if (discs.isEmpty()) {
                status = new StatusMgnt(MSR_OK, "ok");
                discs.add(disc);
            } else {

                boolean duplicate = false;

                ListIterator<Disc> lir = discs.listIterator();

                while (lir.hasNext() && !duplicate) {
                    if (lir.next().getBarcode().equals(disc.getBarcode())) {
                        duplicate = true;
                        status = new StatusMgnt(MSR_BAD_REQUEST, "The disc is already in the system. No duplicate allowed");
                    } else {
                        status = new StatusMgnt(MSR_OK, "ok");
                    }
                }

                if (!duplicate) {
                    discs.add(disc);
                }
            }

        }
        return status;
    }


    @Override
    public List<Book> getBooks() {
        return books;
    }


    @Override
    public Pair<StatusMgnt, Book> getBook(String isbn) {
        Pair<StatusMgnt, Book> myResult = null;

        for (Book b : books) {
            if (b.getIsbn().equals(isbn)) {
                return new Pair<>(new StatusMgnt(MSR_OK, "ok"), b);
            } else {
                myResult = new Pair<>(new StatusMgnt(MSR_NOT_FOUND, "The book you have searched for is not in the system!"), null);
            }
        }
        return myResult;
    }

    @Override
    public Pair<StatusMgnt, Disc> getDisc(String barcode) {
        Pair<StatusMgnt, Disc> myResult = null;

        for (Disc d : discs) {
            if (d.getBarcode().equals(barcode)) {
                myResult = new Pair<>(new StatusMgnt(MSR_OK, "ok"), d);
            } else {
                myResult = new Pair<>(new StatusMgnt(MSR_NOT_FOUND, "The disc you have searched for is not in the system!"), null);
            }
        }
        return myResult;
    }


    @Override
    public List<Disc> getDiscs() {
        return discs;
    }


    @Override
    public StatusMgnt updateBook(String isbn, Book newBook) {
        StatusMgnt status = new StatusMgnt(MSR_INTERNAL_SERVER_ERROR, "An internal error has occurred");


        List<Book> oneBook = getBooks().stream().filter(b ->
                b.getIsbn().equals(isbn)
        ).collect(Collectors.toList());
        if (!oneBook.isEmpty()) {
            if (!newBook.getTitle().isEmpty()) {
                for (Book b : oneBook) {
                    b.setTitle(newBook.getTitle());
                }
                status = new StatusMgnt(MSR_OK, "ok");
            }
            if (!newBook.getAuthor().isEmpty()) {
                for (Book b : oneBook) {
                    b.setAuthor(newBook.getAuthor());
                }
                status = new StatusMgnt(MSR_OK, "ok");
            }
        } else {
            status = new StatusMgnt(MSR_BAD_REQUEST, "The book you want to update is not in the system!");
        }
        return status;
    }


    @Override
    public StatusMgnt updateDisc(String barcode, Disc newDisc) {
        StatusMgnt status = new StatusMgnt(MSR_INTERNAL_SERVER_ERROR, "An internal error has occurred");

        List<Disc> oneDisc = getDiscs().stream().filter(d ->
                d.getBarcode().equals(barcode)
        ).collect(Collectors.toList());


        if (!oneDisc.isEmpty()) {

            if (!newDisc.getDirector().isEmpty()) {
                for (Disc d : oneDisc) {
                    d.setDirector(newDisc.getDirector());
                }
                status = new StatusMgnt(MSR_OK, "ok");
            }
            if (newDisc.getFsk() != -1) {
                for (Disc d : oneDisc) {
                    d.setFsk(newDisc.getFsk());
                }
                status = new StatusMgnt(MSR_OK, "ok");
            }
            if (!newDisc.getTitle().isEmpty()) {
                for (Disc d : oneDisc) {
                    d.setTitle(newDisc.getTitle());
                }
                status = new StatusMgnt(MSR_OK, "ok");
            }
        } else {
            status = new StatusMgnt(MSR_BAD_REQUEST, "The disc you want to update is not in the system!");
        }

        return status;
    }

    @Override
    public MediaServiceResult deleteAll() {
        books.clear();
        discs.clear();
        return MSR_OK;
    }



}
