package edu.hm.bugproducer;


import edu.hm.JettyStarter;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

import static edu.hm.bugproducer.Status.MediaServiceResult.*;

public class RestTestWithMock {

    private static final String NAME = "TestName1";
    private static final String NAME_ALT = "TestName2";
    private static final String TITLE = "TestTitle1";
    private static final String ISBN = "3446193138";
    private static final String URL = "http://localhost:8082";
    private static final String EAN = "9783815820865";
    private static final String EAN_ALT = "9783827317100";
    private static final String INVALID_EAN = "1234";

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

    @After
    public void closeConnection() throws Exception {
        jettyStarter.stopJetty();
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

        assertEquals(MSR_OK.getCode(), response.getStatusLine().getStatusCode());
        assertEquals("{\"result\":\"MSR_OK\",\"msg\":\"ok\",\"code\":200}", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testAddBookWrongAuthor() throws IOException {

        HttpClient client = HttpClientBuilder.create().build();

        JSONObject book = new JSONObject();
        book.put("title", TITLE);
        book.put("author", "");
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

        assertEquals(MSR_BAD_REQUEST.getCode(), response.getStatusLine().getStatusCode());
        assertEquals("{\"result\":\"MSR_BAD_REQUEST\",\"msg\":\"Author or title or ISBN was empty\",\"code\":400}", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testAddBookWrongISBN() throws IOException {

        HttpClient client = HttpClientBuilder.create().build();

        JSONObject book = new JSONObject();
        book.put("title", TITLE);
        book.put("author", NAME);
        book.put("isbn", "1234");


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

        assertEquals(MSR_BAD_REQUEST.getCode(), response.getStatusLine().getStatusCode());
        assertEquals("{\"result\":\"MSR_BAD_REQUEST\",\"msg\":\"ISBN was not valid\",\"code\":400}", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testAddBookDuplicate() throws IOException {

        HttpClient client = HttpClientBuilder.create().build();

        JSONObject book = new JSONObject();
        book.put("title", TITLE);
        book.put("author", NAME_ALT);
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
        HttpResponse response2 = client.execute(addBook);

        assertEquals(MSR_BAD_REQUEST.getCode(), response2.getStatusLine().getStatusCode());
        assertEquals("{\"result\":\"MSR_BAD_REQUEST\",\"msg\":\"The book is already in the system. No duplicate allowed\",\"code\":400}", EntityUtils.toString(response2.getEntity()));
    }

    @Test
    public void testGetBooks() throws IOException {

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL_BOOKS);
        HttpResponse shareItResponse = client.execute(request);
        assertEquals(MSR_OK.getCode(), shareItResponse.getStatusLine().getStatusCode());
        assertEquals("[{\"title\":\"TestTitle1\",\"author\":\"TestName1\",\"isbn\":\"3446193138\"}]", EntityUtils.toString(shareItResponse.getEntity()));
    }

    @Test
    public void testGetBook() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL_BOOKS + ISBN);
        HttpResponse shareItResponse = client.execute(request);

        assertEquals(MSR_OK.getCode(), shareItResponse.getStatusLine().getStatusCode());
        assertEquals("{\"title\":\"TestTitle1\",\"author\":\"TestName1\",\"isbn\":\"3446193138\"}", EntityUtils.toString(shareItResponse.getEntity()));
    }

    @Test
    public void testGetBookWrongISBN() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL_BOOKS + "1234");
        HttpResponse shareItResponse = client.execute(request);

        assertEquals(MSR_NOT_FOUND.getCode(), shareItResponse.getStatusLine().getStatusCode());
        assertEquals("{\"result\":\"MSR_NOT_FOUND\",\"msg\":\"The book you have searched for is not in the system!\",\"code\":404}", EntityUtils.toString(shareItResponse.getEntity()));
    }

    @Test
    public void testUpdateBook() throws IOException {
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


        String want = "{\"result\":\"MSR_OK\",\"msg\":\"ok\",\"code\":200}";
        assertEquals(MSR_OK.getCode(), shareItResponse.getStatusLine().getStatusCode());
        assertEquals(want,EntityUtils.toString(shareItResponse.getEntity()));
    }

    @Test
    public void testUpdateBookNotExists() throws IOException{
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

        HttpPut updateBook = new HttpPut(URL_BOOKS + "123");
        updateBook.setEntity(new StringEntity(compactJws));
        updateBook.addHeader("content-Type", "application/json");
        HttpResponse shareItResponse = client.execute(updateBook);

        String want = "{\"result\":\"MSR_BAD_REQUEST\",\"msg\":\"The book you want to update is not in the system!\",\"code\":400}";
        assertEquals(MSR_BAD_REQUEST.getCode(), shareItResponse.getStatusLine().getStatusCode());
        assertEquals(want,EntityUtils.toString(shareItResponse.getEntity()));
    }

    @Test
    public void testUpdateBookWrongAuthor() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();

        JSONObject book = new JSONObject();
        book.put("title", TITLE);
        book.put("author", "");
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

        String want = "{\"result\":\"MSR_BAD_REQUEST\",\"msg\":\"Author and title are empty!\",\"code\":400}";
        assertEquals(MSR_BAD_REQUEST.getCode(), shareItResponse.getStatusLine().getStatusCode());
        assertEquals(want, EntityUtils.toString(shareItResponse.getEntity()));
    }
}
