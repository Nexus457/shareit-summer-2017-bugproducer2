
package edu.hm.bugproducer.restAPI;

import edu.hm.bugproducer.models.Book;

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
        return Response
                .status(200)
                .entity(books)
                .build();
    }

    @PUT
    @Path("/books/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(Book book) {
        System.out.println("updateBook: " + book.getIsbn());

        boolean changed = false;

        Iterator<Book> iter = books.iterator();

        while (iter.hasNext()) {
            Book book1 = iter.next();

            if (book1.getIsbn().equals(book.getIsbn())) {
                iter.remove();
                changed = true;
            }
        }

        if (changed) {
            books.add(book);
        } else {
            RESPONSE_CODE = 400;
        }

        return Response
                .status(RESPONSE_CODE)
                .build();
    }
}
