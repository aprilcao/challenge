package dao;

import static java.util.stream.Collectors.joining;
import static junit.framework.TestCase.assertTrue;
import org.jdbi.v3.core.Jdbi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import school.dao.*;
import school.model.Course;
import school.model.Enrollment;
import school.model.Semester;
import school.model.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class JdbiEnrollmentDAOTest {

    static final Jdbi jdbi = Jdbi.create("jdbc:hsqldb:mem:mymemdb", "SA", "");
    private DBQueue dbQueue = new DBQueue(jdbi);
    private JdbiEnrollmentDAO dao = new JdbiEnrollmentDAO(jdbi,dbQueue);

    @Before public void setUp() throws Exception {
        jdbi.useHandle(handle -> handle.createScript(DBScript.CREATE_TABLE).execute());
    }

    @Test public void add() throws Exception {
        final JdbiCourseDAO courseDAO = new JdbiCourseDAO(jdbi,dbQueue);
        final Course course = new Course("math", 10);
        courseDAO.add(course);

        final JdbiSemesterDAO semesterDAO = new JdbiSemesterDAO(jdbi,dbQueue);
        final Semester semester = new Semester("2020S1");
        semesterDAO.add(semester);

        UUID studentId = UUID.randomUUID();
        final JdbiStudentDAO studentDAO = new JdbiStudentDAO(jdbi,dbQueue);
        final Student student = new Student(studentId, "cao");
        studentDAO.add(student);

        dao.addEnrollment(new Enrollment(semester,student,course));
        Thread.sleep(100);
        assertFalse(dao.loadAll().isEmpty());

        dao.removeEnrollment(new Enrollment(semester,student,course));
        Thread.sleep(100);
        assertTrue(dao.loadAll().isEmpty());
    }

}