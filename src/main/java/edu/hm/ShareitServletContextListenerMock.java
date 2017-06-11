package edu.hm;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import edu.hm.bugproducer.Status.StatusMgnt;
import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.models.Disc;
import edu.hm.bugproducer.restAPI.media.MediaService;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static edu.hm.bugproducer.Status.MediaServiceResult.*;
import static edu.hm.bugproducer.Status.MediaServiceResult.MSR_OK;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Context Listener to enable usage of google guice together with jersey.
 */


public class ShareitServletContextListenerMock
        extends GuiceServletContextListener {

    private static final String NAME = "TestName1";
    private static final String NAME_ALT = "TestName2";
    private static final String TITLE = "TestTitle1";
    private static final String ISBN = "3446193138";
    private static final String URL = "http://localhost:8082";
    private static final String EAN = "9783815820865";
    private static final String EAN_ALT = "9783827317100";
    private static final String INVALID_ISBN = "1234";
    private static final String INVALID_EAN = "1234";
    private static final int FSK = 6;
    private static final int FSK_ALT = 18;
    private static final Injector INJECTOR
            = Guice.createInjector(new ServletModule() {
        @Override
        protected void configureServlets() {

            List<Book> emptyList = new ArrayList<>();
            Book normalBook = new Book(NAME, ISBN, TITLE);
            Book duplicateBook = new Book(NAME_ALT, ISBN, TITLE);
            Book invalidIsbnBook = new Book(NAME, INVALID_ISBN, TITLE);
            Book noAuthorBook = new Book("", ISBN, TITLE);
            emptyList.add(normalBook);

            MediaService mediaService = mock(MediaService.class);

            when(mediaService.addBook(normalBook)).thenReturn(new StatusMgnt(MSR_OK, "ok"));
            when(mediaService.addBook(duplicateBook)).thenReturn(new StatusMgnt(MSR_BAD_REQUEST, "The book is already in the system. No duplicate allowed"));
            when(mediaService.addBook(invalidIsbnBook)).thenReturn(new StatusMgnt(MSR_BAD_REQUEST, "ISBN was not valid"));
            when(mediaService.addBook(noAuthorBook)).thenReturn(new StatusMgnt(MSR_BAD_REQUEST, "Author or title or ISBN was empty"));
            when(mediaService.getBooks()).thenReturn(emptyList);
            when(mediaService.getBook(ISBN)).thenReturn(new Pair<>(new StatusMgnt(MSR_OK, "ok"), normalBook));
            when(mediaService.getBook("1234")).thenReturn(new Pair<>(new StatusMgnt(MSR_NOT_FOUND, "The book you have searched for is not in the system!"), null));
            when(mediaService.updateBook(ISBN, normalBook)).thenReturn(new StatusMgnt(MSR_OK, "ok"));
            when(mediaService.updateBook(ISBN, noAuthorBook)).thenReturn(new StatusMgnt(MSR_BAD_REQUEST, "Author and title are empty!"));
            when(mediaService.updateBook("", normalBook)).thenReturn(new StatusMgnt(MSR_BAD_REQUEST, "The book you want to update is not in the system!"));
            bind((MediaService.class)).toInstance(mediaService);


            List<Disc> emptyDiscList = new ArrayList<>();
            Disc normalDisc = new Disc(NAME, EAN, TITLE, FSK);
            Disc emptyDisc = new Disc("", "", "", -1);
            Disc invalidEANDisc = new Disc(NAME, INVALID_EAN, TITLE, FSK);
            Disc duplicateDisc = new Disc(NAME_ALT, EAN, TITLE, FSK);
            Disc updateDisc = new Disc(NAME, "", TITLE, FSK);
            Disc emptyUpdateDisc = new Disc("", "", "", FSK);

            when(mediaService.addDisc(normalDisc)).thenReturn(new StatusMgnt(MSR_OK, "ok"));
            when(mediaService.addDisc(emptyDisc)).thenReturn(new StatusMgnt(MSR_BAD_REQUEST, "Barcode or director or title was empty or FSK was less than 0 "));
            when(mediaService.addDisc(invalidEANDisc)).thenReturn(new StatusMgnt(MSR_BAD_REQUEST, "Barcode was not valid"));
            when(mediaService.addDisc(duplicateDisc)).thenReturn(new StatusMgnt(MSR_BAD_REQUEST, "The disc is already in the system. No duplicate allowed"));
            when(mediaService.getDiscs()).thenReturn(emptyDiscList);
            when(mediaService.getDisc(EAN)).thenReturn(new Pair<>(new StatusMgnt(MSR_OK, "ok"), normalDisc));
            when(mediaService.getDisc(EAN_ALT)).thenReturn(new Pair<>(new StatusMgnt(MSR_NOT_FOUND, "The disc you have searched for is not in the system!"), null));
            when(mediaService.updateDisc(EAN, updateDisc)).thenReturn(new StatusMgnt(MSR_OK, "ok"));
            when(mediaService.updateDisc(EAN, emptyUpdateDisc)).thenReturn(new StatusMgnt(MSR_BAD_REQUEST, "Director, Title and FSK are empty!"));
            when(mediaService.updateDisc(EAN_ALT, updateDisc)).thenReturn(new StatusMgnt(MSR_BAD_REQUEST, "The disc you want to update is not in the system!"));
        }
    });

    @Override
    protected Injector getInjector() {
        return INJECTOR;
    }

    /**
     * This method is only required for the HK2-Guice-Bridge in the
     * Application class.
     *
     * @return Injector instance.
     */
    static Injector getInjectorInstance() {
        return INJECTOR;
    }

}