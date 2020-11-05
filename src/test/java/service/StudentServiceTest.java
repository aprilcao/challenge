package service;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;

import school.dao.StudentDAO;
import school.model.Student;
import school.service.StudentService;

public class StudentServiceTest {
    private StudentService studentService;
    @Before
    public void setup(){
        StudentDAO studentDAO = mock(StudentDAO.class);
        studentService = new StudentService(studentDAO);
    }
    @Test public void testAddGet() {
        Student student = studentService.add("John");
        final Student student2 = studentService.get(student.id.toString());
        assertEquals(student,student2);
    }

}