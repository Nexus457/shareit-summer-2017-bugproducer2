package edu.hm.bugproducer;


import edu.hm.bugproducer.models.Book;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Matchers;

import static org.junit.Assert.assertEquals;


public class MockTest {
    @Test
   public void firstTest(){

        Book book = Mockito.mock(Book.class);
        Mockito.when(book.getTitle()).thenReturn("Tolles Buch");

        System.out.print(book.getTitle());

        assertEquals(book.getTitle(),"Tolles Buch");



    }

}
