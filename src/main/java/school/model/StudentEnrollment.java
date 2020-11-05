package school.model;


import school.model.Course;
import school.model.Student;
import school.model.TooManyCoursesException;

import java.util.Collections;

import java.util.HashSet;
import java.util.Set;

public class StudentEnrollment {
    public static final int FULL = 20;

    public final Student student;
    private final Set<Course> courses = new HashSet<>();

    public StudentEnrollment(Student student) {
        this.student = student;
    }

    public void add(Course course){
        final Integer totalCredit = courses.stream().map(c -> c.credit).reduce(0, Integer::sum);
        if(totalCredit + course.credit >FULL){
            throw new TooManyCoursesException();
        }
        courses.add(course);
    }

    public void remove(Course course){
        final boolean success = courses.remove(course);
        if(!success){
            throw new IllegalArgumentException("The student does not enrolled in this course:" + course.name);
        }
    }

    public Set<Course> getCourses(){
        return Collections.unmodifiableSet(courses);
    }

    public boolean containCourse(String course){
        return courses.stream().anyMatch(c->c.name.equals(course));
    }

}