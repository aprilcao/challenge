package service;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;

import school.dao.CourseDAO;
import school.model.Course;
import school.service.CourseService;

public class CourseServiceTest {
    private CourseService courseService;
    @Before
    public void setup(){
        CourseDAO courseDAO = mock(CourseDAO.class);
        courseService = new CourseService(courseDAO);
    }
    @Test public void testAddGet() {
        courseService.add(new Course("math",10));
        final Course math = courseService.get("math");

        assertEquals("math",math.name);
        assertEquals(10,math.credit);
    }

}