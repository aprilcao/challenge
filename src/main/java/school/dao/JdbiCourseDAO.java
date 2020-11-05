
package school.dao;

import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import school.model.Course;

import java.util.List;

public class JdbiCourseDAO implements CourseDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbiCourseDAO.class);
    private static final String allQuery = "select * from COURSE";
    private static final String insertQuery = "insert into COURSE columns(name,credit) values(:name,:credit)";
    private final Jdbi jdbi;
    private final DBQueue workload ;

    public JdbiCourseDAO(Jdbi jdbi,DBQueue workload ) {
        this.jdbi = jdbi;
        this.workload = workload;
    }

    public void add(Course course){
        LOGGER.info(course.name +" has been added to the db");
        workload.offer(handle ->
                handle.createUpdate(insertQuery)
                        .bind("name", course.name)
                        .bind("credit", course.credit)
                        .execute()
        );
    }


    public List<Course> loadAll(){
        return jdbi.withHandle(handle -> handle.createQuery(allQuery).map(
                (rs, ctx) -> new Course(rs.getString("NAME"),rs.getInt("CREDIT")))
                .list());
    }


}
