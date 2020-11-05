package school.dao;

import school.model.Course;

import java.util.List;

public interface CourseDAO {
    public default void add(Course course){
        System.out.println(course.name +" has been added");
    }

    public List<Course> loadAll();
}