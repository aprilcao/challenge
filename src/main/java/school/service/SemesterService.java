package school.service;

import school.dao.SemesterDAO;
import school.model.Semester;

import java.util.concurrent.ConcurrentHashMap;

public class SemesterService {
    // local indexed data
    public final ConcurrentHashMap<String, Semester> semesters = new ConcurrentHashMap<>();
    private final SemesterDAO dao ;

    public SemesterService(SemesterDAO dao) {
        this.dao = dao;
        dao.loadAll().stream().forEach(semester -> semesters.put(semester.name,semester));
    }

    /**
     *  Thread safe action
     *  add data to both memory and db
     * @param name
     */
    public void add(String name){
        if(semesters.containsKey(name)){
            throw new IllegalArgumentException("Semester already exist:" + name);
        }
        final Semester semester = new Semester(name);
        semesters.put(name,semester );
        dao.add(semester);
    }

    public Semester get(String name){
        if(!semesters.containsKey(name)){
            throw new IllegalArgumentException("Semester doesn't exist");
        }
        return semesters.get(name);
    }

}