package dao;

import static java.util.stream.Collectors.joining;
import org.jdbi.v3.core.Jdbi;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import school.dao.DBQueue;
import school.dao.DBScript;
import school.dao.JdbiCourseDAO;
import school.dao.JdbiStudentDAO;
import school.model.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

public class JdbiStudentDAOTest {

    static final Jdbi jdbi = Jdbi.create("jdbc:hsqldb:mem:mymemdb", "SA", "");
    JdbiStudentDAO dao = new JdbiStudentDAO(jdbi,new DBQueue(jdbi));

    @Before public void setUp() throws Exception {
        jdbi.useHandle(handle -> handle.createScript(DBScript.CREATE_TABLE).execute());
    }

    @Test public void add() throws Exception {
        final UUID uuid = UUID.randomUUID();
        dao.add(new Student(uuid,"cao"));
        Thread.sleep(100);
        final List<Student> list = dao.loadAll();
        assertFalse(list.isEmpty());
    }

    @Test public void update() throws Exception{
        final UUID uuid = UUID.randomUUID();
        final Student student = new Student(uuid, "cao");
        dao.add(student);
        final Student updatedStudent = new Student(uuid,"ji");
        dao.update(updatedStudent);
        Thread.sleep(100);
        final List<Student> list = dao.loadAll();
        assertTrue(list.contains(updatedStudent));
    }
}