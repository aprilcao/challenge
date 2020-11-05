
package school.dao;

import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import school.model.Student;

import java.util.List;
import java.util.UUID;

public class JdbiStudentDAO implements StudentDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbiStudentDAO.class);
    private static final String allQuery = "select * from Student";
    private static final String insertQuery = "insert into student columns(id,name) values(:id,:name)";
    private static final String updateQuery = "update student set name = :name where id=:id";
    private final Jdbi jdbi;
    private final DBQueue workload ;

    public JdbiStudentDAO(Jdbi jdbi , DBQueue workload ) {
        this.jdbi = jdbi;
        this.workload = workload;
    }

    public void add(Student student){

        workload.offer(handle ->
                handle.createUpdate(insertQuery)
                        .bind("id", student.id)
                        .bind("name", student.name)
                        .execute()
        );
        LOGGER.info(student.toString() + " has been added to the db");
    }


    public List<Student> loadAll(){
        return jdbi.withHandle(handle -> handle.createQuery(allQuery).map(
                (rs, ctx) -> new Student(UUID.fromString(rs.getString("ID")),rs.getString("NAME")))
                .list());
    }

    public void update(Student student){
        workload.offer(handle ->
                handle.createUpdate(updateQuery)
                        .bind("id", student.id)
                        .bind("name", student.name)
                        .execute()
        );
        LOGGER.info(student.toString()+" has been updated in the db");
    }

}
