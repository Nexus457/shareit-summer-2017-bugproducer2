
package edu.hm.bugproducer.restAPI.media;

import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.models.Disc;
import edu.hm.bugproducer.restAPI.MediaServiceResult;
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
     * @return status code
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
     * @param jwtString  unique string
     * @return statusCode
     */
    @POST
    @Path("/discs/")
    public Response createDiscs(String jwtString) {
        MediaServiceResult result = MediaServiceResult.MSR_INTERNAL_SERVER_ERROR;

        try {
            Disc disc = createDiscOutOfJwt(jwtString);
            result = mediaService.addDisc(disc);
            System.out.println(mediaService.getDiscs());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return Response
                .status(result.getCode())
                .build();
    }

    /**
     * createDiscOutOfJwt
     * creates a disc by using a jwt
     * @param jwtString unique string
     * @return disc object
     * @throws UnsupportedEncodingException by wrong encoding
     */
    private Disc createDiscOutOfJwt(String jwtString) throws UnsupportedEncodingException {
        Jwts.parser()
                .setSigningKey("secret".getBytes("UTF-8"))
                .parseClaimsJws(jwtString);

        String derString = Jwts.parser()
                .setSigningKey("secret".getBytes("UTF-8"))
                .parseClaimsJws(jwtString).getBody().get("disc").toString();

        JSONObject jsonObj = new JSONObject(derString);

        Disc disc = new Disc();
        disc.setDirector(jsonObj.getString("director"));
        disc.setTitle(jsonObj.getString("title"));
        disc.setBarcode(jsonObj.getString("barcode"));
        disc.setFsk(jsonObj.getInt("fsk"));
        return disc;
    }

    /**
     * createBooks method.
     * create books by using the HTTP verb POST
     * @param jwtString  unique string
     * @return statusCode
     */
    @POST
    @Path("/books/")
    public Response createBooks(String jwtString) {
        MediaServiceResult result = MediaServiceResult.MSR_INTERNAL_SERVER_ERROR;

        try {
            Book book = createBookOutOfJwt(jwtString);
            result = mediaService.addBook(book);
            System.out.println(mediaService.getBooks());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return Response
                .status(result.getCode())
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
        Pair<MediaServiceResult, Book> myResult = mediaService.getBook(isbn.replaceAll("-", ""));
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
     * @param jwtString  unique string
     * @return statusCode
     */
    @PUT
    @Path("/books/{isbn}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("isbn") String isbn, String jwtString) {

        MediaServiceResult result = MediaServiceResult.MSR_INTERNAL_SERVER_ERROR;

        System.out.print("Hallo ich bins der update!!!!");
        try {
            Book book = createBookOutOfJwt(jwtString);
            result = mediaService.updateBook(isbn.replaceAll("-", ""), book);
            System.out.println(mediaService.getBooks());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return Response
                .status(result.getCode())
                .build();
    }

    /**
     * createBookOutOfJwt
     * creates a book by using a jwt
     * @param jwtString unique string
     * @return book object
     * @throws UnsupportedEncodingException by wrong encoding
     */
    private Book createBookOutOfJwt(String jwtString) throws UnsupportedEncodingException {
        Jwts.parser()
                .setSigningKey("secret".getBytes("UTF-8"))
                .parseClaimsJws(jwtString);

        String derString = Jwts.parser()
                .setSigningKey("secret".getBytes("UTF-8"))
                .parseClaimsJws(jwtString).getBody().get("book").toString();

        JSONObject jsonObj = new JSONObject(derString);

        Book book = new Book();
        book.setAuthor(jsonObj.getString("author"));
        book.setTitle(jsonObj.getString("title"));
        book.setIsbn(jsonObj.getString("isbn"));
        return book;
    }

    /**
     * updateDisc method.
     * updates the disc by using the HTTP verb PUT
     *
     * @param barcode unique number of a disc
     * @param jwtString unique string
     * @return statusCode
     */
    @PUT
    @Path("/discs/{barcode}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDisc(@PathParam("barcode") String barcode, String jwtString) {

        MediaServiceResult result = MediaServiceResult.MSR_INTERNAL_SERVER_ERROR;

        System.out.print("Hallo ich bins der update!!!!");
        try {
            Disc disc = createDiscOutOfJwt(jwtString);
            result = mediaService.updateDisc(barcode, disc);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return Response
                .status(result.getCode())
                .build();

    }
}
