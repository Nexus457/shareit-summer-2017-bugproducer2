
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
    public Response createCopy(@FormParam("user") String user,
                               @FormParam("code") String code)
                               {

        System.out.println("user: " + user + " ISBN: " + code);

        MediaServiceResult result = copyService.addCopy(user, code);
        System.out.print("Create Copy!!!");

        return Response
                .status(result.getCode())
                .build();
    }

    @GET
    @Path("/books/") //todo change Path because => [FATAL] A HTTP GET method,
    // public javax.ws.rs.core.Response edu.hm.bugproducer.restAPI.copy.CopyResource.getCopy
    // (java.lang.String), should not consume any form parameter.
    @Produces(MediaType.APPLICATION_JSON)
        public Response getCopy() {
        System.out.println("getCopy!!");

        /*Pair<MediaServiceResult, Copy> myResult = copyService.getCopy(code);
                return Response
                .status(myResult.getKey().getCode())
                .entity(myResult.getValue())
                .build();*/
      return null;

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
