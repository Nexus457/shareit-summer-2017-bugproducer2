package edu.hm.bugproducer;


import edu.hm.bugproducer.Status.StatusMgnt;
import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.restAPI.media.MediaService;
import edu.hm.bugproducer.restAPI.media.MediaServiceImpl;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Matchers;

import static edu.hm.bugproducer.Status.MediaServiceResult.MSR_BAD_REQUEST;
import static edu.hm.bugproducer.Status.MediaServiceResult.MSR_NO_CONTENT;
import static edu.hm.bugproducer.Status.MediaServiceResult.MSR_OK;
import static org.junit.Assert.assertEquals;


public class MockTest {
    private static final String USER_NAME = "Joh";
    private static final String NAME = "TestName1";
    private static final String NAME_ALT = "TestName2";
    private static final String TITLE = "TestTitle1";
    private static final String TITLE_ALT = "TestTitle2";
    private static final String ISBN = "3-446-193138";
    private static final String ISBN_ALT = "0-7475-51006";
    private static final String EAN = "9783815820865";
    private static final String EAN_ALT = "9783827317100";

    @Test
   public void testAddBook(){
        MediaService mediaService = new MediaServiceImpl();
        Book book = Mockito.mock(Book.class);
        Mockito.when(book.getTitle()).thenReturn(TITLE);
        Mockito.when(book.getAuthor()).thenReturn(NAME);
        Mockito.when(book.getIsbn()).thenReturn(ISBN);
        StatusMgnt status = mediaService.addBook(book);
        StatusMgnt wanted = new StatusMgnt(MSR_OK, "ok");
        assertEquals(wanted,status);

    }

    @Test
    public void testAddBookNoTitle(){
        MediaService mediaService = new MediaServiceImpl();
        Book book = Mockito.mock(Book.class);
        Mockito.when(book.getTitle()).thenReturn("");
        Mockito.when(book.getAuthor()).thenReturn(NAME);
        Mockito.when(book.getIsbn()).thenReturn(ISBN);
        StatusMgnt status = mediaService.addBook(book);
        StatusMgnt wanted = new StatusMgnt(MSR_BAD_REQUEST, "Author or title or ISBN was empty");

        assertEquals(wanted,status);
    }


    @Test
    public void testAddBookNoAuthor(){
        MediaService mediaService = new MediaServiceImpl();
        Book book = Mockito.mock(Book.class);
        Mockito.when(book.getTitle()).thenReturn(TITLE);
        Mockito.when(book.getAuthor()).thenReturn("");
        Mockito.when(book.getIsbn()).thenReturn(ISBN);
        StatusMgnt status = mediaService.addBook(book);
        StatusMgnt wanted = new StatusMgnt(MSR_BAD_REQUEST, "Author or title or ISBN was empty");
        assertEquals(wanted,status);
    }

    @Test
    public void testAddBookNoISBN(){
        MediaService mediaService = new MediaServiceImpl();
        Book book = Mockito.mock(Book.class);
        Mockito.when(book.getTitle()).thenReturn(TITLE);
        Mockito.when(book.getAuthor()).thenReturn(NAME);
        Mockito.when(book.getIsbn()).thenReturn("");
        StatusMgnt status = mediaService.addBook(book);
        StatusMgnt wanted = new StatusMgnt(MSR_BAD_REQUEST, "Author or title or ISBN was empty");
        assertEquals(wanted,status);
    }

    @Test
    public void testAddBookInValidISBN(){
        MediaService mediaService = new MediaServiceImpl();
        Book book = Mockito.mock(Book.class);
        Mockito.when(book.getTitle()).thenReturn(TITLE);
        Mockito.when(book.getAuthor()).thenReturn(NAME);
        Mockito.when(book.getIsbn()).thenReturn(ISBN_ALT+457);
        StatusMgnt status = mediaService.addBook(book);
        StatusMgnt wanted = new StatusMgnt(MSR_BAD_REQUEST, "ISBN was not valid");
        assertEquals(wanted,status);
    }

    @Test
    public void testAddDoublicateBook(){
        MediaService mediaService = new MediaServiceImpl();
        Book book = Mockito.mock(Book.class);
        Mockito.when(book.getTitle()).thenReturn(TITLE);
        Mockito.when(book.getAuthor()).thenReturn(NAME);
        Mockito.when(book.getIsbn()).thenReturn(ISBN);
        mediaService.addBook(book);
        StatusMgnt status = mediaService.addBook(book);
        StatusMgnt wanted = new StatusMgnt(MSR_BAD_REQUEST, "The book is already in the system. No duplicate allowed");
        assertEquals(wanted,status);

    }





}
