
package school.dao;


import school.model.Enrollment;
import school.model.EnrollmentFlat;

import java.util.List;

public interface EnrollmentDAO {
    public void addEnrollment(Enrollment enrollment);
    public void removeEnrollment(Enrollment enrollment);
    public List<EnrollmentFlat> loadAll();
}