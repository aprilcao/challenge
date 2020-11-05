

package school.dao;

import school.model.Semester;

import java.util.List;

public interface SemesterDAO {
    public void add(Semester semester);

    public List<Semester> loadAll();
}