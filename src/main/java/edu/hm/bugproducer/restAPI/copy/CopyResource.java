
package edu.hm.bugproducer.restAPI.copy;

import edu.hm.bugproducer.models.*;
import edu.hm.bugproducer.restAPI.MediaServiceResult;

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


    @POST
    @Path("/books/")
    public Response createCopy(@FormParam("user") String user,
                               @FormParam("code") String code,
                               @FormParam("lfnr") int lfnr) {

        System.out.println("user: " + user + " ISBN: " + code + " LFNR: " + lfnr);

        MediaServiceResult result = copyService.addCopy(user, code, lfnr);
        System.out.print("Create Copy!!!");

        return Response
                .status(result.getCode())
                .build();
    }

   /* @Path("/discs/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCopy(@QueryParam("user") String user, Book book) {
        MediaServiceResult result = copyService.addCopy(book, user);
        return Response
                .status(result.getCode())
                .build();
    }*/
}
