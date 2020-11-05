package service;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import static junit.framework.TestCase.assertTrue;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;

import school.dao.CourseDAO;
import school.dao.EnrollmentDAO;
import school.dao.SemesterDAO;
import school.dao.StudentDAO;
import school.model.Course;
import school.model.Semester;
import school.model.Student;
import school.service.CourseService;
import school.service.EnrollmentService;
import school.service.SemesterService;
import school.service.StudentService;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class EnrollmentServiceTest {

    private final CourseService courseService = new CourseService(mock(CourseDAO.class));
    private final SemesterService semesterService = new SemesterService(mock(SemesterDAO.class));
    private final StudentService studentService = new StudentService(mock(StudentDAO.class));
    private final EnrollmentService enrollmentService = new EnrollmentService(mock(EnrollmentDAO.class),courseService,semesterService,studentService);
    final Student cao = studentService.add("cao");
    final Student jon = studentService.add("jon");
    @Before public void setUp() throws Exception {
        semesterService.add("2020S1");
        semesterService.add("2021S1");
        courseService.add(new Course("math",10));
        courseService.add(new Course("english",10));
        courseService.add(new Course("physics",10));
        courseService.add(new Course("chemistry",10));

    }

    @Test public void enroll() {
        enrollmentService.enroll("2020S1",cao.id.toString(),"math");
        enrollmentService.enroll("2020S1",cao.id.toString(),"english");
        Set<Course> courses = enrollmentService.queryStudent(cao.id, "2020S1");
        assertEquals(2,courses.size());
        Assert.assertTrue(courses.contains(new Course("math",10)));
        Assert.assertTrue(courses.contains(new Course("english",10)));

        enrollmentService.remove("2020S1",cao.id.toString(),"math");

        enrollmentService.queryStudent(cao.id, "2020S1");
        assertEquals(1,courses.size());
        Assert.assertFalse(courses.contains(new Course("math",10)));
        Assert.assertTrue(courses.contains(new Course("english",10)));
        enrollmentService.remove("2020S1",cao.id.toString(),"english");
    }

    @Test public void queryStudentForAllSemesters() {
        enrollmentService.enroll("2020S1",cao.id.toString(),"math");
        enrollmentService.enroll("2020S1",cao.id.toString(),"english");
        enrollmentService.enroll("2021S1",cao.id.toString(),"physics");
        enrollmentService.enroll("2021S1",cao.id.toString(),"chemistry");

        Map<Semester, Set<Course>> expected = ImmutableMap.of(
                new Semester("2020S1"), ImmutableSet.of(new Course("math",10),new Course("english",10)),
                new Semester("2021S1"), ImmutableSet.of(new Course("physics",10),new Course("chemistry",10))
        );

        assertEquals(expected,enrollmentService.queryStudentForAllSemesters(UUID.fromString(cao.id.toString())));

        assertEquals(expected.get(new Semester("2020S1")),enrollmentService.queryStudent(UUID.fromString(cao.id.toString()), "2020S1"));
        enrollmentService.remove("2020S1",cao.id.toString(),"math");
        enrollmentService.remove("2020S1",cao.id.toString(),"english");
        enrollmentService.remove("2021S1",cao.id.toString(),"physics");
        enrollmentService.remove("2021S1",cao.id.toString(),"chemistry");

    }

    @Test public void queryCourse() {
        enrollmentService.enroll("2020S1",cao.id.toString(),"math");
        enrollmentService.enroll("2020S1",cao.id.toString(),"english");
        enrollmentService.enroll("2020S1",jon.id.toString(),"english");

        final Set<Student> students = enrollmentService.queryCourse("english", "2020S1");
        assertEquals(2,students.size());
        assertTrue(students.contains(cao));
        assertTrue(students.contains(jon));
        enrollmentService.remove("2020S1",cao.id.toString(),"math");
        enrollmentService.remove("2020S1",cao.id.toString(),"english");
        enrollmentService.remove("2020S1",jon.id.toString(),"english");
    }


    @Test
    public void testTooManyCourses(){
        enrollmentService.enroll("2020S1",cao.id.toString(),"math");
        enrollmentService.enroll("2020S1",cao.id.toString(),"english");
        try {
            enrollmentService.enroll("2020S1", cao.id.toString(), "physics");
            fail();
        }catch(Exception e){
            enrollmentService.remove("2020S1",cao.id.toString(),"math");
            enrollmentService.remove("2020S1",cao.id.toString(),"english");
        }
    }
}