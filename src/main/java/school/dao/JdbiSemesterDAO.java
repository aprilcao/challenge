
package school.dao;

import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import school.model.Semester;

import java.util.List;

public class JdbiSemesterDAO implements SemesterDAO{
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbiSemesterDAO.class);

    private static final String allQuery = "select * from semester";
    private static final String insertQuery = "insert into semester columns(name) values(:name)";
    private final Jdbi jdbi;
    private final DBQueue workload ;
    public JdbiSemesterDAO(Jdbi jdbi, DBQueue workload) {
        this.jdbi = jdbi;
        this.workload = workload;
    }

    public void add(Semester semester){
        LOGGER.info(semester.name +" has been added");
        workload.offer(handle ->
                handle.createUpdate(insertQuery)
                        .bind("name", semester.name)
                        .execute()
        );
    }


    public List<Semester> loadAll(){
        return jdbi.withHandle(handle -> handle.createQuery(allQuery).map(
                (rs, ctx) -> new Semester(rs.getString("NAME")))
                .list());
    }
}