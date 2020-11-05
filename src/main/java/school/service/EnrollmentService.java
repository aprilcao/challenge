
package school.service;

import school.model.SemesterEnrollmentList;
import school.model.StudentEnrollment;
import school.dao.EnrollmentDAO;
import school.model.Course;
import school.model.Enrollment;
import school.model.Semester;
import school.model.Student;

import static java.util.stream.Collectors.toMap;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EnrollmentService {
    // local indexed data
    private final ConcurrentHashMap<Semester, SemesterEnrollmentList> semesterEnrollmentMap = new ConcurrentHashMap<>();
    private final EnrollmentDAO dao;
    private final CourseService courseService;
    private final SemesterService semesterService;
    private final StudentService studentService;

    public EnrollmentService(EnrollmentDAO dao, CourseService courseService, SemesterService semesterService,
                             StudentService studentService) {
        this.dao = dao;
        this.courseService = courseService;
        this.semesterService = semesterService;
        this.studentService = studentService;
        dao.loadAll().stream().forEach(enrollment -> enrollWithoutDB(semesterService.get(enrollment.semester),
                studentService.get(enrollment.studentId), courseService.get(enrollment.course)));
    }

    /**
     * Not thread-safe.
     * @param semester
     * @param student
     * @param course
     */
    private void enrollWithoutDB(Semester semester, Student student, Course course){
        semesterEnrollmentMap.putIfAbsent(semester,new SemesterEnrollmentList());
        final StudentEnrollment studentEnrollment = semesterEnrollmentMap.get(semester).getStudentEnrollment(student);
        studentEnrollment.add(course);
    }

    /**
     * Thread-safe.
     * It would need lock to enroll courses for same student. and for all the other situation it is lock free.
     * @param semesterName
     * @param studentId
     * @param courseName
     */
    public void enroll(String semesterName, String studentId, String courseName){
        Semester semester = semesterService.get(semesterName);
        Student student = studentService.get(studentId);
        Course course = courseService.get(courseName);
        semesterEnrollmentMap.putIfAbsent(semester,new SemesterEnrollmentList());
        final StudentEnrollment studentEnrollment = semesterEnrollmentMap.get(semester).getStudentEnrollment(student);
        synchronized (studentEnrollment){
            studentEnrollment.add(course);
            dao.addEnrollment(new Enrollment(semester,student,course));
        }
    }

    public void remove(String semesterName, String studentId, String courseName){
        Semester semester = semesterService.get(semesterName);
        Student student = studentService.get(studentId);
        Course course = courseService.get(courseName);
        if(!semesterEnrollmentMap.containsKey(semester)){
            throw new IllegalArgumentException("Semester doesn't exist:" + semester.name);
        }

        final StudentEnrollment studentEnrollment = semesterEnrollmentMap.get(semester).getStudentEnrollment(student);
        synchronized (studentEnrollment){
            studentEnrollment.remove(course);
            dao.removeEnrollment(new Enrollment(semester,student,course));
        }

    }

    public Set<Course> queryStudent(UUID studentId,String semesterName) {
        final Semester semester = semesterService.get(semesterName);
        if(semester!=null && !semesterEnrollmentMap.containsKey(semester)){
            return Collections.emptySet();
        }

        return semesterEnrollmentMap.get(semester).queryStudent(studentId);
    }

    public Map<Semester,Set<Course>> queryStudentForAllSemesters(UUID studentId) {
        return semesterEnrollmentMap.entrySet().stream()
                .collect(toMap(entry -> entry.getKey(), entry -> entry.getValue().queryStudent(studentId)));

    }

    public Set<Student> queryCourse(String courseName,String semesterName){
        Course course = courseService.get(courseName);
        Semester semester = semesterService.get(semesterName);
        if(!semesterEnrollmentMap.containsKey(semester)){
            return Collections.emptySet();
        }
        return semesterEnrollmentMap.get(semester).queryCourse(course.name);
    }

    public Map<Semester,Set<Student>> queryCourseForAllSemesters(String courseName){
        return semesterEnrollmentMap.entrySet().stream()
                .collect(toMap(entry -> entry.getKey(), entry -> entry.getValue().queryCourse(courseName)));

    }
}