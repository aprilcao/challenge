package school.rest;

import school.model.Course;
import school.service.CourseService;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Objects;

@Path("/course")
public class CourseRestService {

    private final CourseService courseService;

    public CourseRestService(CourseService courseService) {
        this.courseService = courseService;
    }

    @POST
    @Path("/add")
    @Produces(MediaType.TEXT_PLAIN)
    public Response add(@QueryParam("name") String name, @QueryParam("credit") int credit) {
        try {
            Objects.requireNonNull(name);
            courseService.add(new Course(name, credit));
            return Response.ok().entity("success").build();
        }catch(IllegalArgumentException |NullPointerException e){
            return Response.status(BAD_REQUEST).entity(e.getMessage()).build();
        }catch(Exception e){
            return Response.status(INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}