

package school.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Semester {
    public final String name;
    @JsonCreator
    public Semester(@JsonProperty("name") String name) {
        this.name = Objects.requireNonNull(name);
    }

    @Override public String toString() {
        return  name ;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Semester semester = (Semester) o;
        return name.equals(semester.name);
    }

    @Override public int hashCode() {
        return Objects.hash(name);
    }
}