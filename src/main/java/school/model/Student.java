

package school.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.UUID;

public class Student {
    public final UUID id;
    public final String name;

    @JsonCreator
    public Student(@JsonProperty("id") UUID id, @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Student student = (Student) o;
        return id.equals(student.id) && name.equals(student.name);
    }

    @Override public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override public String toString() {
        return "Student{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}




