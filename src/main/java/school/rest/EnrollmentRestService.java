
package school.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import school.service.EnrollmentService;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Objects;
import java.util.UUID;

@Path("/enrollment")
public class EnrollmentRestService {
    private final EnrollmentService enrollmentService;
    ObjectMapper objectMapper = new ObjectMapper();
    public EnrollmentRestService(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;

    }

    @POST
    @Path("/add")
    @Produces(MediaType.TEXT_PLAIN)
    public Response add(@QueryParam("studentId") String studentId, @QueryParam("course") String course,
            @QueryParam("semester") String semester) {
        try {
            Objects.requireNonNull(studentId);
            Objects.requireNonNull(course);
            Objects.requireNonNull(semester);
            enrollmentService.enroll(semester, studentId, course);
            return Response.ok("success").build();
        } catch (IllegalArgumentException | NullPointerException e) {
            return Response.status(BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/remove")
    @Produces(MediaType.TEXT_PLAIN)
    public Response remove(@QueryParam("studentId") String studentId, @QueryParam("course") String course,
            @QueryParam("semester") String semester) {
        try {
            enrollmentService.remove(semester, studentId, course);
            return Response.ok("success").build();
        } catch (IllegalArgumentException | NullPointerException e) {
            return Response.status(BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/queryStudent")
    @Produces(MediaType.APPLICATION_JSON)
    public Response queryStudent(@QueryParam("studentId") String studentId, @QueryParam("semester") String semester) {
        try {
            Object result = (semester == null)
                    ? enrollmentService.queryStudentForAllSemesters(UUID.fromString(studentId))
                    : enrollmentService.queryStudent(UUID.fromString(studentId), semester);

            return Response.ok(objectMapper.writeValueAsString(result)).build();

        } catch (IllegalArgumentException | NullPointerException e) {
            return Response.status(BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/queryCourse")
    @Produces(MediaType.APPLICATION_JSON)
    public Response queryCourse(@QueryParam("course") String courseName, @QueryParam("semester") String semester) {
        try {
            Object result = semester == null
                    ? enrollmentService.queryCourseForAllSemesters(courseName)
                    : enrollmentService.queryCourse(courseName,semester);

            return Response.ok(objectMapper.writeValueAsString(result)).build();
        } catch (IllegalArgumentException | NullPointerException e) {
            return Response.status(BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}