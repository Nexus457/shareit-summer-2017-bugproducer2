
package edu.hm.bugproducer.restAPI.media;

import edu.hm.bugproducer.Status.MediaServiceResult;
import edu.hm.bugproducer.Status.StatusMgnt;
import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.models.Disc;
import io.jsonwebtoken.Jwts;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static edu.hm.bugproducer.Status.MediaServiceResult.MSR_INTERNAL_SERVER_ERROR;
import static edu.hm.bugproducer.Status.MediaServiceResult.MSR_OK;

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
     * Object variable for the Logger
     */
    private static final Logger LOGGER = LogManager.getLogger(MediaResource.class.getName());

    /**
     * response code for OK
     */
    private static final int RESPONSECODE = 200;

    /**
     * mediaService variable for the media service implementation
     */
    private final MediaService mediaService;

    /**
     * MediaResource Constructor.
     *
     * @param mediaService aktueller service
     */
    @Inject
    public MediaResource(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    /**
     * Delete all lists.
     *
     * @return status code
     */
    @GET
    @Path("/reset")
    public Response deleteAll() {
        LOGGER.info("Delete all tables");
        MediaServiceResult result = mediaService.deleteAll();
        return Response
                .status(result.getCode())
                .build();
    }

    /**
     * createDiscs method.
     * create a disc by using the HTTP verb POST
     *
     * @param jwtString unique string
     * @return statusCode
     */
    @POST
    @Path("/discs/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDiscs(String jwtString) {
        StatusMgnt result = new StatusMgnt(MSR_INTERNAL_SERVER_ERROR, "An internal error has occurred");

        try {
            Disc disc = createDiscOutOfJwt(jwtString);
            result = mediaService.addDisc(disc);
            LOGGER.info("Disc created");

        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Something went wrong", e);
            e.printStackTrace();
        }

        return Response
                .status(result.getCode())
                .entity(result)
                .build();
    }

    /**
     * createBook method.
     * create books by using the HTTP verb POST
     *
     * @param jwtString unique string
     * @return statusCode
     */
    @POST
    @Path("/books/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBook(String jwtString) {
        StatusMgnt result = new StatusMgnt(MSR_INTERNAL_SERVER_ERROR, "An internal error has occurred");

        try {
            Book book = createBookOutOfJwt(jwtString);
            result = mediaService.addBook(book);
            LOGGER.info("Book created");

        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Something went wrong", e);
            e.printStackTrace();
        }

        return Response
                .status(result.getCode())
                .entity(result)
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
        LOGGER.info("Get all books");
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
        LOGGER.info("Get all discs");
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
        Pair<StatusMgnt, Book> myResult = mediaService.getBook(isbn.replaceAll("-", ""));
        System.out.println(isbn);
        if (myResult.getKey().getCode() == MSR_OK.getCode()) {
            LOGGER.info("Get book " + isbn);
            return Response
                    .status(myResult.getKey().getCode())
                    .entity(myResult.getValue())
                    .build();
        } else {
            LOGGER.warn("Get book " + isbn + " went wrong");
            return Response
                    .status(myResult.getKey().getCode())
                    .entity(myResult.getKey())
                    .build();
        }
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
        Pair<StatusMgnt, Disc> myResult = mediaService.getDisc(barcode);
        System.out.println(barcode);
        if (myResult.getKey().getCode() == MSR_OK.getCode()) {
            LOGGER.info("Get disc " + barcode);
            return Response
                    .status(myResult.getKey().getCode())
                    .entity(myResult.getValue())
                    .build();
        } else {
            LOGGER.warn("Get disc " + barcode + " went wrong");
            return Response
                    .status(myResult.getKey().getCode())
                    .entity(myResult.getKey())
                    .build();

        }
    }

    /**
     * updateBook method.
     * updates the book by using the HTTP verb PUT
     *
     * @param isbn      unique number of book
     * @param jwtString unique string
     * @return statusCode
     */
    @PUT
    @Path("/books/{isbn}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("isbn") String isbn, String jwtString) {
        StatusMgnt result = new StatusMgnt(MSR_INTERNAL_SERVER_ERROR, "An internal error has occurred");

        try {
            Book book = createBookOutOfJwt(jwtString);
            result = mediaService.updateBook(isbn.replaceAll("-", ""), book);
            LOGGER.info("Book " + isbn + " updated");

        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Update book went wrong", e);
            e.printStackTrace();
        }

        return Response
                .status(result.getCode())
                .entity(result)
                .build();
    }


    /**
     * updateDisc method.
     * updates the disc by using the HTTP verb PUT
     *
     * @param barcode   unique number of a disc
     * @param jwtString unique string
     * @return statusCode
     */
    @PUT
    @Path("/discs/{barcode}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDisc(@PathParam("barcode") String barcode, String jwtString) {
        StatusMgnt result = new StatusMgnt(MSR_INTERNAL_SERVER_ERROR, "An internal error has occurred");

        try {
            Disc disc = createDiscOutOfJwt(jwtString);
            result = mediaService.updateDisc(barcode, disc);
            LOGGER.info("Disc " + barcode + " updated");

        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Update book went wrong", e);
            e.printStackTrace();
        }

        return Response
                .status(result.getCode())
                .entity(result)
                .build();
    }


    /**
     * createDiscOutOfJwt
     * creates a disc by using a jwt
     *
     * @param jwtString unique string
     * @return disc object
     * @throws UnsupportedEncodingException by wrong encoding
     */
    private Disc createDiscOutOfJwt(String jwtString) throws UnsupportedEncodingException {
        LOGGER.trace("Create Disc out of JWT");
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
     * createBookOutOfJwt
     * creates a book by using a jwt
     *
     * @param jwtString unique string
     * @return book object
     * @throws UnsupportedEncodingException by wrong encoding
     */
    private Book createBookOutOfJwt(String jwtString) throws UnsupportedEncodingException {
        LOGGER.trace("Create Book out of JWT");
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
}
