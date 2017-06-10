package edu.hm;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import edu.hm.bugproducer.Status.StatusMgnt;
import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.restAPI.media.MediaService;
import edu.hm.bugproducer.restAPI.media.MediaServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static edu.hm.bugproducer.Status.MediaServiceResult.MSR_INTERNAL_SERVER_ERROR;
import static edu.hm.bugproducer.Status.MediaServiceResult.MSR_OK;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Context Listener to enable usage of google guice together with jersey.
 */


public class ShareitServletContextListenerMock
        extends GuiceServletContextListener {

    private static final String NAME = "TestName1";
    private static final String TITLE = "TestTitle1";
    private static final String ISBN = "3446193138";
    private static final String URL = "http://localhost:8082";
    private static final String EAN = "9783815820865";

    private static final Injector INJECTOR
            = Guice.createInjector(new ServletModule() {
        @Override
        protected void configureServlets() {

            List<Book> emptyList = new ArrayList<>();

            MediaService mediaService = mock(MediaService.class);
            Book book = new Book(NAME, ISBN, TITLE);
            when(mediaService.addBook(book)).thenReturn(new StatusMgnt(MSR_OK, "ok"));
            when(mediaService.getBooks()).thenReturn(emptyList);
            bind((MediaService.class)).toInstance(mediaService);


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