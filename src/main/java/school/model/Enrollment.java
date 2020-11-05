
package school.model;

import java.util.Objects;

public class Enrollment {
    public final Semester semester;
    public final Student student;
    public final Course course;

    public Enrollment(Semester semester, Student student, Course course) {
        this.semester = semester;
        this.student = student;
        this.course = course;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Enrollment that = (Enrollment) o;
        return semester.equals(that.semester) && student.equals(that.student) && course.equals(that.course);
    }

    @Override public int hashCode() {
        return Objects.hash(semester, student, course);
    }

    @Override public String toString() {
        return "Enrollment{" + "semester=" + semester + ", student=" + student + ", course=" + course + '}';
    }
}