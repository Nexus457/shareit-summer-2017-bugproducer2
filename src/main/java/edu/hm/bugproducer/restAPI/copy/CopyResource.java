
package edu.hm.bugproducer.restAPI.copy;

import edu.hm.bugproducer.models.*;
import edu.hm.bugproducer.restAPI.MediaServiceResult;
import edu.hm.bugproducer.restAPI.media.MediaService;
import javafx.util.Pair;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * CopyResource Class.
 * @author Mark Tripolt
 * @author Johannes Arzt
 * @author Tom Maier
 * @author Patrick Kuntz
 */
@Path("/copy")
public class CopyResource {
    /** HTTP Responsecode of OK */
    private static final int RESPONSECODE = 200;
    /** ArrayList of Typ Book with contains the books */
    private static List<Book> books = new ArrayList<>();

    private CopyServiceImpl copyService = new CopyServiceImpl();

    /**
     * CopyResource Constructor.
     */
    public CopyResource() {

    }

    /**
     * getCopies method of type Response.
     * gives the status code and the
     * List of type Copy back by using the HTTP verb GET
     * @return copyList list with real copies of medium
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCopies() {
        System.out.println("getCopies!!");

        List<Copy> copyList = copyService.getCopies();
        return Response
                .status(RESPONSECODE)
                .entity(copyList)
                .build();

    }

    /**
     * createCopyBook method of type Reponse.
     * creates a real copy of a existing book by using HTTP verb POST
     * @param user person who borrow the copy
     * @param code the isbn of the book
     * @return statusCode the response of creating a book
     */
    @POST
    @Path("/books/")
    public Response createCopyBook(@FormParam("user") String user,
                               @FormParam("code") String code) {

        System.out.println("user: " + user + " ISBN: " + code);

        MediaServiceResult result = copyService.addCopy(user, code);
        System.out.print("Create Copy Book!!!");

        return Response
                .status(result.getCode())
                .build();
    }
    /**
     * createCopyDisc method of type Reponse.
     * creates a real copy of a existing disc by using HTTP verb POST
     * @param user person who borrow the copy
     * @param code the barcode of the disc
     * @return statusCode the response of creating a disc
     */

    @POST
    @Path("/discs/")
    public Response createCopyDisc(@FormParam("user") String user,
                               @FormParam("code") String code) {

        System.out.println("user: " + user + " EAN: " + code);

        MediaServiceResult result = copyService.addCopy(user, code);
        System.out.print("Create Copy Disc!!!");

        return Response
                .status(result.getCode())
                .build();
    }

    /**
     * getCopyBook method of type Repsone.
     * returns the status code and the exact copy by using the HTTP verb GET
     * @param isbn unique number of book
     * @param lfnr running number for the copy
     * @return statusCode and the copy
     */
    @GET
    @Path("/books/{code}/{lfnr}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCopyBook(@PathParam("code") String isbn, @PathParam("lfnr") int lfnr) {
        System.out.println(isbn);
        System.out.print(lfnr);

        Pair<MediaServiceResult, Copy> myResult = copyService.getCopy(isbn, lfnr);
                return Response
                .status(myResult.getKey().getCode())
                .entity(myResult.getValue())
                .build();
    }

    /**
     * getCopyDisc method of type Repsone.
     * returns the status code and the exact copy by using the HTTP verb GET
     * @param isbn unique number of book
     * @param lfnr running number for the copy
     * @return statusCode and the copy
     */
    @GET
    @Path("/discs/{code}/{lfnr}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCopyDisc(@PathParam("code") String isbn, @PathParam("lfnr") int lfnr) {
        System.out.println(isbn);
        System.out.print(lfnr);

        Pair<MediaServiceResult, Copy> myResult = copyService.getCopy(isbn, lfnr);
        return Response
                .status(myResult.getKey().getCode())
                .entity(myResult.getValue())
                .build();
    }

    /**
     * getCopyBook method of type Response.
     * update the information user, isbn and lfnr of a book by using the HTTP verb PUT
     * @param user new person who borrows the book
     * @param isbn unique number of a book
     * @param lfnr running number of an exact copy of a book
     * @return statusCode response of updating a book
     */
    @PUT
    @Path("/books/{code}/{lfnr}")
    public Response getCopyBook(@FormParam("user") String user, @PathParam("code") String isbn, @PathParam("lfnr") int lfnr) {

        MediaServiceResult result = copyService.updateCopy(user,isbn,lfnr);

        return Response
                .status(result.getCode())
                .build();

    }

    /**
     * getCopyDisc method of type Response.
     * update the information user, isbn and lfnr of a disc by using the HTTP verb PUT
     * @param user new person who borrows the disc
     * @param isbn unique number of a disc
     * @param lfnr running number of an exact copy of a disc
     * @return statusCode response of updating a disc
     */
    @PUT
    @Path("/discs/{code}/{lfnr}")
    public Response getCopyDisc(@FormParam("user") String user, @PathParam("code") String isbn, @PathParam("lfnr") int lfnr) {

        MediaServiceResult result = copyService.updateCopy(user,isbn,lfnr);

        return Response
                .status(result.getCode())
                .build();

    }







}
