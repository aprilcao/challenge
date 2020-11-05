package service;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;

import school.dao.SemesterDAO;
import school.model.Semester;
import school.service.SemesterService;

public class SemesterServiceTest {
    private SemesterService semesterService;
    @Before
    public void setup(){
        SemesterDAO semesterDAO = mock(SemesterDAO.class);
        semesterService = new SemesterService(semesterDAO);
    }
    @Test public void testAddGet() {
        semesterService.add("2020S1");
        final Semester semester = semesterService.get("2020S1");
        assertEquals("2020S1",semester.name);
    }

}