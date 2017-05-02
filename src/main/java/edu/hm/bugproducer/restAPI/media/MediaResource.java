
package edu.hm.bugproducer.restAPI.media;

import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.models.Disc;
import edu.hm.bugproducer.restAPI.MediaServiceResult;
import javafx.util.Pair;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/media")
public class MediaResource {
    private static final int RESPONSECODE = 200;
    // static, weil bei jedem Methodenaufruf ein neues Objekt erstellt wird.
    private static List<Book> books = new ArrayList<>();

    private MediaServiceImpl mediaService = new MediaServiceImpl();

    public MediaResource() {
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
                .status(RESPONSECODE)
                .entity(bookList)
                .build();
    }

    @GET
    @Path("/books/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("isbn") String isbn) {
        System.out.println("getBook");
        Pair<MediaServiceResult, Book> myResult = mediaService.getBook(isbn);
        System.out.println(isbn);
        return Response
                .status(myResult.getKey().getCode())
                .entity(myResult.getValue())
                .build();
    }

    @GET
    @Path("/discs/{barcode}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDisc(@PathParam("barcode") String barcode) {
        System.out.println("getBook");
        Pair<MediaServiceResult, Disc> myResult = mediaService.getDisc(barcode);
        System.out.println(barcode);
        return Response
                .status(myResult.getKey().getCode())
                .entity(myResult.getValue())
                .build();
    }

    @PUT
    @Path("/books/{isbn}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("isbn") String isbn, Book book) {
        System.out.println("updateBook: " + isbn);
        System.out.println("Title: " + book.getTitle() + "Author: " + book.getAuthor() + " ISBN: " + book.getIsbn());
        MediaServiceResult result = mediaService.updateBook(isbn, book);
        System.err.println("RESULT" + result.getCode());
        return Response
                .status(result.getCode())
                .build();
    }

    @PUT
    @Path("/discs/{barcode}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDisc(@PathParam("barcode") String barcode, Disc disc) {
        MediaServiceResult result = mediaService.updateDisc(barcode, disc);
        System.err.println("RESULT" + result.getCode());
        return Response
                .status(result.getCode())
                .build();
    }
}
