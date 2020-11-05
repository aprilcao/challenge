package dao;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import org.jdbi.v3.core.Jdbi;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import school.dao.DBQueue;
import school.dao.DBScript;
import school.dao.JdbiCourseDAO;
import school.model.Course;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class JdbiCourseDAOTest {
    final static Jdbi jdbi = Jdbi.create("jdbc:hsqldb:mem:mymemdb", "SA", "");
    private JdbiCourseDAO dao = new JdbiCourseDAO(jdbi,new DBQueue(jdbi));
    @Before public void setUp() throws Exception {
        jdbi.useHandle(handle -> handle.createScript(DBScript.CREATE_TABLE).execute());
    }


    @Test public void test() throws Exception {
        dao.add(new Course("english",10));
        Thread.sleep(100);
        final List<Course> list = dao.loadAll();
        assertEquals("english",list.get(0).name);
    }

}