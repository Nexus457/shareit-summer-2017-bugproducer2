
package edu.hm.bugproducer.restAPI;

import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.models.Disc;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Path("/media")
public class MediaResource {

    // static, weil bei jedem Methodenaufruf ein neues Objekt erstellt wird.
    private static List<Book> books = new ArrayList<>();
    private static int RESPONSE_CODE = 200;
    private MediaServiceImpl mediaService = new MediaServiceImpl();

    public MediaResource() {
    }

    @POST
    @Path("/books/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createBooks(Book book) {
        //System.out.println("createBooks" + book.getAuthor());
        //ToDo Change Name
        MediaServiceResult result = mediaService.addBook(book);


        return Response
                .status(result.getCode())
                .build();


    }

    @GET
    @Path("/books/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooks() {
        System.out.println("getBooks");
        List<Book> bookList = mediaService.getBooks();
        return Response
                .status(200)
                .entity(bookList)
                .build();
    }

    @PUT
    @Path("/books/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(Book book) {
        System.out.println("updateBook: " + book.getIsbn());

        MediaServiceResult result = mediaService.updateBook(book);
        System.err.println("RESULT" + result.getCode());
        return Response
                .status(result.getCode())
                .build();
    }
    @POST
    @Path("/discs/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDiscs(Disc disc) {
        //System.out.println("createBooks" + book.getAuthor());
        //ToDo Change Name
        MediaServiceResult result = mediaService.addDisc(disc);
        return Response
                .status(result.getCode())
                .build();



    }

}
