
package edu.hm.bugproducer.restAPI.media;

import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.models.Disc;
import edu.hm.bugproducer.restAPI.MediaServiceResult;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import javafx.util.Pair;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * MediaResource Class.
 *
 * @author Mark Tripolt
 * @author Johannes Arzt
 * @author Tom Maier
 * @author Patrick Kuntz
 */
@Path("/media")
public class MediaResource {
    /**
     * response code for OK
     */
    private static final int RESPONSECODE = 200;
    // static, weil bei jedem Methodenaufruf ein neues Objekt erstellt wird.
    /**
     * ArrayList which contains books
     */
    private static List<Book> books = new ArrayList<>();
    /**
     * mediaService variable for the media service implementation
     */
    private MediaServiceImpl mediaService = new MediaServiceImpl();

    /**
     * MediaResource Constructor.
     */
    public MediaResource() {
    }

    /**
     * Delete all lists.
     *
     * @return
     */
    @GET
    @Path("/reset")
    public Response deleteAll() {
        System.out.println("deleteAll");
        MediaServiceResult result = mediaService.deleteAll();
        return Response
                .status(result.getCode())
                .build();
    }

    /**
     * createDiscs method.
     * create a disc by using the HTTP verb POST
     *
     * @param disc disc object
     * @return statusCode
     */
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

    /**
     * createBooks method.
     * create books by using the HTTP verb POST
     *
     * @param book book object
     * @return statusCode
     */
    @POST
    @Path("/books/")
    public Response createBooks(String jwtString) {

        try {
            Jwts.parser()
                    .setSigningKey("secret".getBytes("UTF-8"))
                    .parseClaimsJws(jwtString);

            String  derString = Jwts.parser()
                    .setSigningKey("secret".getBytes("UTF-8"))
                    .parseClaimsJws(jwtString).getBody().get("book").toString();

            JSONObject jsonObj = new JSONObject(derString);

            Book book = new Book();


            book.setAuthor(jsonObj.getString("author"));
            book.setTitle(jsonObj.getString("title"));
            book.setIsbn(jsonObj.getString("isbn"));


            //TODO HIER WEITERMACHEN !!!!!!!!!!!!!!!eins eins elf



            System.out.print(book.getIsbn());
            System.out.print(book.getAuthor());
            System.out.print(book.getTitle());


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        //System.out.println("createBooks" +"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        //ToDo Change Name



        MediaServiceResult result = null;

        return Response
                .status(200)
                .build();
    }

    /**
     * getBooks method.
     * gets the list with books by using the HTTP verb GET
     *
     * @return statusCode and the bookList entity
     */
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

    /**
     * getDiscs method.
     * gets the list with discs by using the HTTP verb GET
     *
     * @return statusCode and the discList entitiy
     */
    @GET
    @Path("/discs/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscs() {
        System.out.println("getDiscs");
        List<Disc> discList = mediaService.getDiscs();
        return Response
                .status(RESPONSECODE)
                .entity(discList)
                .build();
    }


    /**
     * getBook method.
     * gets the book from mediaService with a specific isbn by using the HTTP verb GET
     *
     * @param isbn unique number of a book
     * @return statusCode and the book entity
     */
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

    /**
     * getDisc method.
     * gets a disc from mediaService with a specific barcode by using the HTTP verb GET
     *
     * @param barcode unique number of a disc
     * @return statusCode and the disc entity
     */
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

    /**
     * updateBook method.
     * updates the book by using the HTTP verb PUT
     *
     * @param isbn unique number of book
     * @param book book object
     * @return statusCode
     */
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

    /**
     * updateDisc method.
     * updates the disc by using the HTTP verb PUT
     *
     * @param barcode unique number of a disc
     * @param disc    disc object
     * @return statusCode
     */
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
