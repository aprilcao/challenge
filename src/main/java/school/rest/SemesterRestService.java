
package school.rest;

import school.service.SemesterService;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Objects;

@Path("/semester")
public class SemesterRestService {
    private final SemesterService semesterService;

    public SemesterRestService(SemesterService semesterService) {
        this.semesterService = semesterService;
    }

    @POST
    @Path("/add")
    @Produces(MediaType.TEXT_PLAIN)
    public Response add(@QueryParam("name") String name) {
        try {
            Objects.requireNonNull(name);
            semesterService.add(name);
            return Response.ok().entity("success").build();
        }catch(IllegalArgumentException|NullPointerException e){
            return Response.status(BAD_REQUEST).entity(e.getMessage()).build();
        }catch(Exception e){
            return Response.status(INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

}
