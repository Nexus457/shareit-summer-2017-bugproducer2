package edu.hm.bugproducer.restAPI.media;

import edu.hm.bugproducer.Status.MediaServiceResult;
import edu.hm.bugproducer.Status.StatusMgnt;
import edu.hm.bugproducer.Utils.Isbn;
import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.models.Disc;
import edu.hm.bugproducer.persistenceLayer.HibernateUtil;
import javafx.util.Pair;
import org.apache.commons.validator.routines.checkdigit.EAN13CheckDigit;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
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

    private Session session = HibernateUtil.getSessionFactory().getCurrentSession();

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

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Book bookDB = session.get(Book.class, book.getIsbn());
            session.getTransaction().commit();

            if (bookDB == null) {
                System.err.println("check");
                Session session2 = HibernateUtil.getSessionFactory().getCurrentSession();
                session2.beginTransaction();
                session2.save(book);
                session2.getTransaction().commit();

                status = new StatusMgnt(MSR_OK, "ok");

            } else {
                status = new StatusMgnt(MSR_BAD_REQUEST, "The book is already in the system. No duplicate allowed");
            }

        }

        return status;
    }


    @Override
    public StatusMgnt addDisc(Disc disc) {
        StatusMgnt status = new StatusMgnt(MSR_INTERNAL_SERVER_ERROR, "An internal error has occurred");

        if (disc == null) {
            status = new StatusMgnt(MSR_NO_CONTENT, "The disc was empty");
        } else if (disc.getBarcode().isEmpty() || disc.getDirector().isEmpty() || disc.getTitle().isEmpty() || disc.getFsk() < 0) {
            status = new StatusMgnt(MSR_BAD_REQUEST, "Barcode or director or title was empty or FSK was less than 0 ");
        } else if (!EAN13CheckDigit.EAN13_CHECK_DIGIT.isValid(disc.getBarcode())) {
            status = new StatusMgnt(MSR_BAD_REQUEST, "Barcode was not valid");
        } else {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Disc discDB = session.get(Disc.class, disc.getBarcode());
            session.getTransaction().commit();

            if (discDB == null) {
                Session session2 = HibernateUtil.getSessionFactory().getCurrentSession();
                session2.beginTransaction();
                session2.save(disc);
                session2.getTransaction().commit();

                status = new StatusMgnt(MSR_OK, "ok");

            } else {
                status = new StatusMgnt(MSR_BAD_REQUEST, "The disc is already in the system. No duplicate allowed");
            }
        }
        return status;
    }


    @Override
    public List<Book> getBooks() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Book> resultBooks = session.createCriteria(Book.class).list();

        return resultBooks;
    }


    @Override
    public Pair<StatusMgnt, Book> getBook(String isbn) {
        Pair<StatusMgnt, Book> myResult = new Pair<>(new StatusMgnt(MSR_NOT_FOUND, "The book you have searched for is not in the system!"), null);

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Book bookDB = session.get(Book.class, isbn);
        session.getTransaction().commit();
        if (bookDB != null) {
            return new Pair<>(new StatusMgnt(MSR_OK, "ok"), bookDB);
        } else
            return myResult;
    }

    @Override
    public Pair<StatusMgnt, Disc> getDisc(String barcode) {
        Pair<StatusMgnt, Disc> myResult = new Pair<>(new StatusMgnt(MSR_NOT_FOUND, "The disc you have searched for is not in the system!"), null);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Disc discDB = session.get(Disc.class, barcode);
        session.getTransaction().commit();
        if (discDB != null) {
            return new Pair<>(new StatusMgnt(MSR_OK, "ok"), discDB);
        } else
            return myResult;
    }


    @Override
    public List<Disc> getDiscs() {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Disc> resultDiscs = session.createCriteria(Disc.class).list();
        return resultDiscs;
    }


    @Override
    public StatusMgnt updateBook(String isbn, Book newBook) {
        StatusMgnt status = new StatusMgnt(MSR_INTERNAL_SERVER_ERROR, "An internal error has occurred");

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Book oneBook = session.get(Book.class, isbn);
        session.getTransaction().commit();


        if (oneBook != null) {
            if (newBook.getTitle().isEmpty() || newBook.getAuthor().isEmpty()) {
                //TOdo change this! There are more details
                status = new StatusMgnt(MSR_BAD_REQUEST, "Author and title are empty!");
            } else {

                Session session2 = HibernateUtil.getSessionFactory().getCurrentSession();
                session2.beginTransaction();
                session2.update(newBook);
                session2.getTransaction().commit();
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

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Disc oneDisc = session.get(Disc.class, barcode);
        session.getTransaction().commit();


        if (oneDisc != null) {
            if (newDisc.getDirector().isEmpty() && newDisc.getTitle().isEmpty() && newDisc.getFsk() == -1) {
                status = new StatusMgnt(MSR_BAD_REQUEST, "Director, Title and FSK are empty!");
            } else {
                Session session2 = HibernateUtil.getSessionFactory().getCurrentSession();
                session2.beginTransaction();
                session2.update(newDisc);
                session2.getTransaction().commit();
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
