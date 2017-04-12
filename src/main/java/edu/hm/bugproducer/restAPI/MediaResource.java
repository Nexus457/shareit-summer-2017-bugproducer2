package edu.hm.bugproducer.restAPI;

import edu.hm.bugproducer.models.Book;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/media")
public class MediaResource {

    public MediaResource() {
    }

    @POST
    @Path("/books/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createBooks(Book book){
        System.out.println("createBooks" + book.getAuthor());


        return null;
    }


}
