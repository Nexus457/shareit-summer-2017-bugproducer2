package edu.hm.bugproducer.modules;


import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import edu.hm.bugproducer.Status.StatusMgnt;
import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.restAPI.media.MediaService;


import static edu.hm.bugproducer.Status.MediaServiceResult.MSR_INTERNAL_SERVER_ERROR;
import static edu.hm.bugproducer.Status.MediaServiceResult.MSR_OK;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModuleTest extends AbstractModule {
    @Override
    protected void configure() {


    }

    @Provides
    MediaService test() {
        MediaService mediaService = mock(MediaService.class);
        Book book = new Book("Hans", "123", "Das geht ?");
        Book booktwo = new Book("Hans", "12", "Das geht ?");
        when(mediaService.addBook(book)).thenReturn(new StatusMgnt(MSR_OK, "ok"));
        when(mediaService.addBook(booktwo)).thenReturn(new StatusMgnt(MSR_INTERNAL_SERVER_ERROR, "upsi"));
        return mediaService;

    }

}
