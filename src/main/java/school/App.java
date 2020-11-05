package school;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import school.dao.*;
import school.rest.CourseRestService;
import school.rest.EnrollmentRestService;
import school.rest.SemesterRestService;
import school.rest.StudentRestService;
import school.service.CourseService;
import school.service.EnrollmentService;
import school.service.SemesterService;
import school.service.StudentService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private static final String PORT = "APP_PORT";

    public static void main(String[] args) {
        try{
            Server server = getServer();
            server.start();
            server.join();
        } catch (Exception e) {
            LOGGER.error("Fatal error:", e);
            System.exit(-1);
        }
    }



    public static Server getServer() {
        String path = System.getProperty("APP_PATH",".");
        final Jdbi jdbi  = getJdbi(path);
        final DBQueue dbQueue = new DBQueue(jdbi);
        initDB(jdbi);
        final CourseService courseService = new CourseService(new JdbiCourseDAO(jdbi,dbQueue));
        final CourseRestService courseRestService = new CourseRestService(courseService);

        final SemesterService semesterService = new SemesterService(new JdbiSemesterDAO(jdbi,dbQueue));
        final SemesterRestService semesterRestService = new SemesterRestService(semesterService);

        final StudentService studentService = new StudentService(new JdbiStudentDAO(jdbi,dbQueue));
        final StudentRestService studentRestService = new StudentRestService(studentService);

        final EnrollmentService enrollmentService = new EnrollmentService(new JdbiEnrollmentDAO(jdbi,dbQueue),courseService,semesterService,studentService);
        final EnrollmentRestService enrollmentRestService = new EnrollmentRestService(enrollmentService);

        final ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(courseRestService);
        resourceConfig.register(semesterRestService);
        resourceConfig.register(studentRestService);
        resourceConfig.register(enrollmentRestService);

        ServletContextHandler sch = new ServletContextHandler();
        sch.setContextPath("/");
        sch.addServlet(new ServletHolder(new ServletContainer(resourceConfig)), "/*");

        Server server = new Server(Integer.parseInt(System.getProperty(PORT,"80")));
        server.setHandler(sch);
        return server;
    }

    private static Jdbi getJdbi(String path){
        final String mode = System.getProperty("STORE_MODE");
        if(mode!=null && mode.equals("memory")){
            return Jdbi.create("jdbc:hsqldb:mem:mymemdb", "SA", "");
        }
        return Jdbi.create(format("jdbc:hsqldb:file:%s", Paths.get(path).resolve("db").toAbsolutePath()));
    }

    public static void initDB( Jdbi jdbi) {
        jdbi.useHandle(handle -> handle.createScript(DBScript.CREATE_TABLE).execute());
    }
}