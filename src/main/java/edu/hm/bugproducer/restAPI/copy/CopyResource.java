
package edu.hm.bugproducer.restAPI.copy;

import edu.hm.bugproducer.models.*;
import edu.hm.bugproducer.restAPI.MediaServiceResult;
import javafx.util.Pair;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;


@Path("/copy")
public class CopyResource {
    private static final int RESPONSECODE = 200;
    private static List<Book> books = new ArrayList<>();


    private CopyServiceImpl copyService = new CopyServiceImpl();

    public CopyResource() {

    }


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







}
