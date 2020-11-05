
package school.model;



import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SemesterEnrollmentList {
    private final ConcurrentHashMap<UUID, StudentEnrollment> enrollments = new ConcurrentHashMap<>();


    public StudentEnrollment getStudentEnrollment(Student student){
        enrollments.putIfAbsent(student.id, new StudentEnrollment(student));
        return enrollments.get(student.id);
    }

    public Set<Course> queryStudent(UUID uuid){
        if(!enrollments.containsKey(uuid)){
            return Collections.EMPTY_SET;
        }
        return enrollments.get(uuid).getCourses();
    }
    public Set<Student> queryCourse(String name){
        return enrollments.values().stream()
                .filter(enrollment -> enrollment.containCourse(name))
                .map(enrollment -> enrollment.student)
                .collect(Collectors.toSet());
    }
}