package edu.hm.bugproducer;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.hm.JettyStarter;
import edu.hm.bugproducer.restAPI.media.MediaService;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RestTestWithMock {

    private static final String NAME = "TestName1";
    private static final String TITLE = "TestTitle1";
    private static final String ISBN = "3-446-193138";
    private static final String URL = "http://localhost:8082";
    private static final String EAN = "9783815820865";

    private static final String WRONG_ISBN = "0-7475006";
    private static final String WRONG_EAN = "3-446-19313";

    private static final String URL_BOOKS = "http://localhost:8080/shareit/media/books/";
    private static final String URL_DISCS = "http://localhost:8080/shareit/media/discs/";

    private JettyStarter jettyStarter;

    @Before
    public void openConnection() throws Exception {
        jettyStarter = new JettyStarter();
        jettyStarter.startJetty();
    }


    @Test
    public void testAddBook() throws IOException {

        HttpClient client = HttpClientBuilder.create().build();

        JSONObject book = new JSONObject();
        book.put("title", TITLE);
        book.put("author", NAME);
        book.put("isbn", ISBN);


        Map<String, Object> headerClaims = new HashMap();
        headerClaims.put("type", Header.JWT_TYPE);
        String compactJws = null;

        try {
            compactJws = Jwts.builder()
                    .claim("book", book.toString())
                    .setHeader(headerClaims)
                    .signWith(SignatureAlgorithm.HS256, "secret".getBytes("UTF-8"))
                    .compact();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpPost addBook = new HttpPost(URL_BOOKS);

        addBook.setEntity(new StringEntity(compactJws));
        addBook.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(addBook);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetBooks() throws IOException {

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL_BOOKS);
        HttpResponse shareItResponse = client.execute(request);
        System.err.println("Ergebnis: " + EntityUtils.toString(shareItResponse.getEntity()));
        assertEquals(200, shareItResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetBook() throws IOException{
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL_BOOKS + ISBN);
        HttpResponse shareItResponse = client.execute(request);
        System.err.println("Ergebnis: " + EntityUtils.toString(shareItResponse.getEntity()));
        assertEquals(200, shareItResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testUpdateBook() throws IOException{
        HttpClient client = HttpClientBuilder.create().build();

        JSONObject book = new JSONObject();
        book.put("title", TITLE);
        book.put("author", NAME);
        book.put("isbn", ISBN);


        Map<String, Object> headerClaims = new HashMap();
        headerClaims.put("type", Header.JWT_TYPE);
        String compactJws = null;

        try {
            compactJws = Jwts.builder()
                    .claim("book", book.toString())
                    .setHeader(headerClaims)
                    .signWith(SignatureAlgorithm.HS256, "secret".getBytes("UTF-8"))
                    .compact();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpPut updateBook = new HttpPut(URL_BOOKS + ISBN);
        updateBook.setEntity(new StringEntity(compactJws));
        updateBook.addHeader("content-Type", "application/json");
        HttpResponse shareItResponse = client.execute(updateBook);
        System.out.println(shareItResponse.getStatusLine().getStatusCode());
    }
}
