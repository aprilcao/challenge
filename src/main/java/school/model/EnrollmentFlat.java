
package school.model;

import java.util.Objects;

public class EnrollmentFlat {
    public final String semester;
    public final String studentId;
    public final String course;

    public EnrollmentFlat(String semester, String studentId, String course) {
        this.semester = semester;
        this.studentId = studentId;
        this.course = course;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        EnrollmentFlat that = (EnrollmentFlat) o;
        return semester.equals(that.semester) && studentId.equals(that.studentId) && course.equals(that.course);
    }

    @Override public int hashCode() {
        return Objects.hash(semester, studentId, course);
    }
}