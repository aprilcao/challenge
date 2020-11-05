package dao;

import static java.util.stream.Collectors.joining;
import org.jdbi.v3.core.Jdbi;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import school.dao.DBQueue;
import school.dao.DBScript;
import school.dao.JdbiSemesterDAO;
import school.model.Semester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class JdbiSemesterDAOTest {
    static final Jdbi jdbi = Jdbi.create("jdbc:hsqldb:mem:mymemdb", "SA", "");
    private JdbiSemesterDAO dao = new JdbiSemesterDAO(jdbi,new DBQueue(jdbi));

    @Before public void setUp() throws Exception {
        jdbi.useHandle(handle -> handle.createScript(DBScript.CREATE_TABLE).execute());
    }


    @Test public void add() throws Exception{
        dao.add(new Semester("2020S1"));
        Thread.sleep(100);
        final List<Semester> list = dao.loadAll();
        assertEquals("2020S1",list.get(0).name);
    }
}