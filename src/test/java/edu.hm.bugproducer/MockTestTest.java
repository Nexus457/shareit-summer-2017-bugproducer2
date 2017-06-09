package edu.hm.bugproducer;


import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.hm.bugproducer.Status.StatusMgnt;
import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.modules.ModuleTest;
import edu.hm.bugproducer.restAPI.media.MediaService;
import org.junit.Test;

import static edu.hm.bugproducer.Status.MediaServiceResult.MSR_OK;
import static org.junit.Assert.assertEquals;


public class MockTestTest {

    static Injector injector = Guice.createInjector(new ModuleTest());

    MediaService test = injector.getInstance(MediaService.class);

    @Test
    public void firstRNDTest(){
        Book book = new Book("Hans", "123", "Das geht ?");
        StatusMgnt want = new StatusMgnt(MSR_OK, "ok");
        assertEquals(want,test.addBook(book));
    }

}
