
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
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCopy(Medium medium, User user) {
        MediaServiceResult result = copyService.addCopy(medium, user);
        return Response
                .status(result.getCode())
                .build();
    }
}
