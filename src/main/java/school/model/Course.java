package school.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Course {
    public final String name;
    public final int credit;
    @JsonCreator
    public Course(@JsonProperty("name") String name, @JsonProperty("credit") int credit) {
        this.name = Objects.requireNonNull(name);
        if(credit<0){
            throw new IllegalArgumentException("credit of the course should be positive number");
        }
        this.credit = credit;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Course course = (Course) o;
        return credit == course.credit && Objects.equals(name, course.name);
    }

    @Override public int hashCode() {
        return Objects.hash(name, credit);
    }

    @Override public String toString() {
        return "Course{" + "name='" + name + '\'' + ", credit=" + credit + '}';
    }
}