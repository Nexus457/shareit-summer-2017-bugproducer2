
package edu.hm.bugproducer.restAPI.copy;

import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.models.Disc;
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

    private CopyServiceImpl mediaService = new CopyServiceImpl();

    public CopyResource() {
    }
/*
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
    }*/
}
