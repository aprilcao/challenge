package school.dao;

import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import school.model.Enrollment;
import school.model.EnrollmentFlat;

import java.util.List;


/**
 * CREATE TABLE IF NOT EXISTS COURSE(
 *     NAME VARCHAR(128),
 *     CREDIT INT,
 *     PRIMARY KEY (NAME)
 * );
 *
 * CREATE TABLE IF NOT EXISTS SEMESTER(
 *     NAME VARCHAR(128),
 *     PRIMARY KEY (NAME)
 * );
 *
 * CREATE TABLE IF NOT EXISTS STUDENT(
 *     ID VARCHAR(32),
 *     NAME VARCHAR(128),
 *     PRIMARY KEY (ID)
 * );
 *
 * CREATE TABLE IF NOT EXISTS ENROLLMENT(
 *     STUDENT_ID VARCHAR(32),
 *     SEMESTER VARCHAR(128),
 *     COURSE VARCHAR(128),
 *     PRIMARY KEY (STUDENT_ID,SEMESTER,COURSE)
 * );
 */

public class JdbiEnrollmentDAO implements EnrollmentDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbiEnrollmentDAO.class);
    private static final String allQuery = "select * from enrollment";
    private static final String insertQuery = "insert into enrollment columns(student_id,semester,course) values(:student_id,:semester,:course)";
    private static final String deleteQuery = "delete from enrollment where student_id=:student_id and semester=:semester and course = :course" ;

    private final Jdbi jdbi;
    private final DBQueue workload ;
    public JdbiEnrollmentDAO(Jdbi jdbi,DBQueue dbQueue) {
        this.jdbi = jdbi;
        this.workload = dbQueue;
    }

    @Override public void addEnrollment(Enrollment enrollment) {
        workload.offer(handle ->
                handle.createUpdate(insertQuery)
                        .bind("student_id", enrollment.student.id)
                        .bind("semester", enrollment.semester.name)
                        .bind("course", enrollment.course.name)
                        .execute()
        );
        LOGGER.info(enrollment.semester.name+" "+ enrollment.student.name + " " + enrollment.course.name +" has been added");
    }

    @Override public void removeEnrollment(Enrollment enrollment) {
        workload.offer(handle ->
                handle.createUpdate(deleteQuery)
                        .bind("student_id", enrollment.student.id)
                        .bind("semester", enrollment.semester.name)
                        .bind("course", enrollment.course.name)
                        .execute()
        );
        LOGGER.info(enrollment.semester.name+" "+ enrollment.student.name + " " + enrollment.course.name +" has been removed");

    }

    public List<EnrollmentFlat> loadAll(){
        return jdbi.withHandle(handle -> handle.createQuery(allQuery)
                .map( (rs, ctx) -> new EnrollmentFlat(rs.getString("semester"),
                        rs.getString("student_id"),
                        rs.getString("course"))
                )
                .list());
    }


}