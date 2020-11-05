
package school.dao;

import school.model.Student;

import java.util.List;


public interface StudentDAO {
    public void add(final Student student);

    public List<Student> loadAll();
}