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

public class SemesterRestServiceTest {
    private final static int PORT = 83;
    @Before public void setUp() throws Exception {
        System.setProperty("APP_PORT",String.valueOf(PORT));
        System.setProperty("STORE_MODE","memory");
        new Thread(() -> App.main(null)).start();
        Thread.sleep(3000);
    }
    
    @Test public void add() throws Exception{
        // Given
        HttpUriRequest request = new HttpPost( "http://localhost:"+PORT+"/semester/add?name=2025S1" );

        // When
        HttpResponse response = HttpClientBuilder.create().build().execute( request );
        String text = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8)).lines()
                .collect(Collectors.joining("\n"));
        System.out.println(text);
        assertEquals(200,response.getStatusLine().getStatusCode());

        // Then


        assertFalse(text.isEmpty());
    }
}