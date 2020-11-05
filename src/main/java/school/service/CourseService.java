

package school.service;


import school.dao.CourseDAO;
import school.model.Course;


import java.util.concurrent.ConcurrentHashMap;

public class CourseService {
    private final ConcurrentHashMap<String,Course> courses = new ConcurrentHashMap<>();
    private final CourseDAO dao;

    public CourseService(CourseDAO dao) {
        this.dao = dao;
        dao.loadAll().stream().forEach(course -> courses.put(course.name,course));
    }

    /**
     * Thread safe action
     * add data to both memory and db
     * @param course
     */
    public void add(Course course){
        if(courses.containsKey(course.name)){
            throw new IllegalArgumentException("Course already exist:" + course.name);
        }
        courses.put(course.name,course);
        dao.add(course);
    }

    public Course get(String name){
        if(!courses.containsKey(name)){
            throw new IllegalArgumentException("Course doesn't exist");
        }
        return courses.get(name);
    }
}