package rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import school.App;
import school.model.Course;
import school.model.Semester;
import school.model.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EnrollmentRestServiceTest {
    private final static int PORT = 82;
    ObjectMapper objectMapper = new ObjectMapper();
    @Before public void setUp() throws Exception {
        System.setProperty("APP_PORT",String.valueOf(PORT));
        System.setProperty("STORE_MODE","memory");
        new Thread(() -> App.main(null)).start();
        Thread.sleep(3000);
    }

    @Test public void add() throws Exception{
        HttpResponse response;
        HttpClientBuilder.create().build().execute( new HttpPost( "http://localhost:"+PORT+"/course/add?name=science&credit=2" ) );
        HttpClientBuilder.create().build().execute( new HttpPost( "http://localhost:"+PORT+"/course/add?name=english&credit=3" ) );
        HttpClientBuilder.create().build().execute( new HttpPost( "http://localhost:"+PORT+"/semester/add?name=2021S1" ) );

        String studentId1 = addStudent("ji");
        String studentId2 = addStudent("jon");

        response = HttpClientBuilder.create().build().execute( new HttpPost( "http://localhost:"+PORT+"/enrollment/add?course=english&semester=2021S1&studentId="+studentId1 ) );
        assertEquals(200,response.getStatusLine().getStatusCode());

        HttpClientBuilder.create().build().execute( new HttpPost( "http://localhost:"+PORT+"/enrollment/add?course=english&semester=2021S1&studentId="+studentId2 ) );
        HttpClientBuilder.create().build().execute( new HttpPost( "http://localhost:"+PORT+"/enrollment/add?course=science&semester=2021S1&studentId="+studentId1 ) );

        response = HttpClientBuilder.create().build().execute(new HttpGet(
                "http://localhost:"+PORT+"/enrollment/queryStudent?studentId=" + studentId1));

        
        Map<Semester, List<Course>> courses = objectMapper.readValue(getResponse(response),
                new TypeReference<HashMap<Semester, List<Course>>>() {
                });
        assertEquals(2, courses.get(new Semester("2021S1")).size());

        response = HttpClientBuilder.create().build().execute(new HttpGet(
                "http://localhost:"+PORT+"/enrollment/queryCourse?course=english" ));
        String result = getResponse(response);
        System.out.println(result);
        Map<Semester, List<Student>> students = objectMapper.readValue(result,
                new TypeReference<HashMap<Semester, List<Student>>>() {
                });
        assertEquals(2,students.get(new Semester("2021S1")).size());
    }

    private String addStudent(String name) throws IOException {
        HttpResponse studentResponse = HttpClientBuilder.create().build().execute( new HttpPost( "http://localhost:"+PORT+"/student/add?name="+ name) );
        return getResponse(studentResponse).replace("\"","");
    }

    private String getResponse(HttpResponse response)throws IOException{
        return new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8)).lines().collect(
                Collectors.joining("\n"));
    }



}