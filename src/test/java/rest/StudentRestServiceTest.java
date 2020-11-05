package rest;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import school.App;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class StudentRestServiceTest {
    private final static int PORT = 84;
    @Before public void setUp() throws Exception {
        System.setProperty("APP_PORT",String.valueOf(PORT));
        System.setProperty("STORE_MODE","memory");
        new Thread(() -> App.main(null)).start();
        Thread.sleep(3000);
    }
    
    @Test public void add() throws Exception{
        // Given
        HttpUriRequest request = new HttpPost( "http://localhost:"+PORT+"/student/add?name=2020S1" );

        // When
        HttpResponse response = HttpClientBuilder.create().build().execute( request );
        assertEquals(200,response.getStatusLine().getStatusCode());

        // Then
        String text = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8)).lines()
                .collect(Collectors.joining("\n"));

        assertFalse(text.isEmpty());
    }
}