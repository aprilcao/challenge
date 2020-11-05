package school.service;

import school.dao.StudentDAO;
import school.model.Student;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StudentService {
    // local indexed data
    public final ConcurrentHashMap<UUID, Student> students = new ConcurrentHashMap<>();
    private final StudentDAO dao;

    public StudentService(StudentDAO dao) {
        this.dao = dao;
        dao.loadAll().stream().forEach(student -> students.put(student.id,student));
    }

    /**
     * Assumption: student with same name is also acceptable.
     * @param name
     * @return
     */
    public Student add(String name){
        UUID id = UUID.randomUUID();
        final Student student = new Student(id, name);
        students.put(id,student);
        dao.add(student);
        return student;
    }

    public Student get(String id){
        final UUID uuid = UUID.fromString(id);
        if(!students.containsKey(uuid)){
            throw new IllegalArgumentException("student doesn't exist");
        }
        return students.get(uuid);
    }

    public void update(String id, String name){

    }

}