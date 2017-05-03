
package edu.hm.bugproducer.restAPI.copy;

import edu.hm.bugproducer.models.*;
import edu.hm.bugproducer.restAPI.MediaServiceResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    @Path("/{user}/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCopy(@PathParam("user") String user, Disc disc) {
        //MediaServiceResult result = copyService.addCopy(disc, user);
     System.out.print("Create Copy!!!");
        return  null;
       /* return Response
                .status(result.getCode())
                .build();*/
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
