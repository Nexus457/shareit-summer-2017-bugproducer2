package edu.hm.bugproducer;

import edu.hm.JettyStarter;
import edu.hm.bugproducer.restAPI.media.MediaServiceImpl;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.apache.http.HttpHeaders.USER_AGENT;
import static org.junit.Assert.assertEquals;

public class RestTest {

    private static final String NAME = "TestName1";
    private static final String NAME_ALT = "TestName2";
    private static final String TITLE = "TestTitle1";
    private static final String TITLE_ALT = "TestTitle2";
    private static final String ISBN = "3-446-193138";
    private static final String ISBN_ALT = "0-7475-51006";
    private static final String URL = "http://localhost:8082";
    private static final String EAN = "9783815820865";
    private static final String EAN_ALT = "9783827317100";
    private static final String URL_BOOKS = "http://localhost:8082/shareit/media/books/";
    private static final String URL_DISCS = "http://localhost:8082/shareit/media/discs/";

    private JettyStarter jettyStarter;

    @Before
    public void openConnection() throws Exception {

        MediaServiceImpl.books.clear();
        MediaServiceImpl.discs.clear();

        jettyStarter = new JettyStarter();
        jettyStarter.startJetty();

    }

    @After
    public void closeConnection() throws Exception {
        jettyStarter.stopJetty();
    }

    @Test
    public void testConnection() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response = client.execute(request);

        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        /*BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }*/
    }

    @Test
    public void testCreateBook() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", TITLE);
        jsonObject.put("author", NAME);
        jsonObject.put("isbn", ISBN_ALT);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URL_BOOKS);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(httpPost);
        System.out.println("testCreateBook: ");
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testCreateDisc() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("director", NAME);
        jsonObject.put("barcode", EAN);
        jsonObject.put("title", TITLE);
        jsonObject.put("fsk", "16");

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URL_DISCS);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(httpPost);
        System.out.println("testCreateDisc: ");
        System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testCreateBookEmpty() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "");
        jsonObject.put("author", "");
        jsonObject.put("isbn", "");

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URL_BOOKS);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(httpPost);
        System.out.println("testCreateBookEmpty: ");
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testCreateDiscEmpty() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "");
        jsonObject.put("barcode", "");
        jsonObject.put("director", "");
        jsonObject.put("fsk", "");

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URL_DISCS);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(httpPost);
        System.out.println("testCreateDiscEmpty: ");
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testCreateBookDuplicate() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", TITLE);
        jsonObject.put("author", NAME);
        jsonObject.put("isbn", ISBN);

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("title", TITLE);
        jsonObject1.put("author", NAME);
        jsonObject1.put("isbn", ISBN);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URL_BOOKS);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(httpPost);
        System.out.println("testeCreateBookDuplicate: ");
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        HttpPost httpPost1 = new HttpPost(URL_BOOKS);
        httpPost1.setEntity(new StringEntity(jsonObject1.toString()));
        httpPost1.addHeader("content-Type", "application/json");
        HttpResponse response1 = client.execute(httpPost1);
        System.out.println("testCreateBookDuplicate: ");
        System.out.println("Response Code 1: " + response1.getStatusLine().getStatusCode());

        assertEquals(400, response1.getStatusLine().getStatusCode());
    }

    @Test
    public void testCreateTwoBooks() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", TITLE);
        jsonObject.put("author", NAME);
        jsonObject.put("isbn", ISBN);

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("title", TITLE);
        jsonObject1.put("author", NAME);
        jsonObject1.put("isbn", ISBN_ALT);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URL_BOOKS);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(httpPost);
        System.out.println("testeCreateBookDuplicate: ");
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        HttpPost httpPost1 = new HttpPost(URL_BOOKS);
        httpPost1.setEntity(new StringEntity(jsonObject1.toString()));
        httpPost1.addHeader("content-Type", "application/json");
        HttpResponse response1 = client.execute(httpPost1);
        System.out.println("testCreateBookDuplicate: ");
        System.out.println("Response Code 1: " + response1.getStatusLine().getStatusCode());

        assertEquals(200, response1.getStatusLine().getStatusCode());
        assertEquals(200, response.getStatusLine().getStatusCode());
    }


    @Test
    public void testCreateDiscDuplicate() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", TITLE);
        jsonObject.put("barcode", EAN);
        jsonObject.put("director", NAME);
        jsonObject.put("fsk", "16");

        JSONObject jsonObject1 = new JSONObject();
        jsonObject.put("title", TITLE);
        jsonObject.put("barcode", EAN);
        jsonObject.put("director", NAME);
        jsonObject.put("fsk", "16");

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URL_DISCS);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(httpPost);
        System.out.println("testCreateDiscEmpty: ");
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());


        HttpPost httpPost1 = new HttpPost(URL_DISCS);
        httpPost1.setEntity(new StringEntity(jsonObject1.toString()));
        httpPost1.addHeader("content-Type", "application/json");
        HttpResponse response1 = client.execute(httpPost);
        System.out.println("testCreateDiscEmpty: ");
        System.out.println("Response Code : " + response1.getStatusLine().getStatusCode());

        assertEquals(400, response1.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetBook() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", TITLE);
        jsonObject.put("author", NAME);
        jsonObject.put("isbn", ISBN_ALT);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URL_BOOKS);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        client.execute(httpPost);


        HttpGet request = new HttpGet(URL_BOOKS + ISBN_ALT);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response2 = client.execute(request);

        JSONObject wantedBook = new JSONObject(EntityUtils.toString(response2.getEntity()));

        assertEquals(jsonObject.toString(), wantedBook.toString());


    }

    public void testGetNonExistentBook() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", TITLE);
        jsonObject.put("author", NAME);
        jsonObject.put("isbn", ISBN_ALT);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URL_BOOKS);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        client.execute(httpPost);


        HttpGet request = new HttpGet(URL_BOOKS + ISBN_ALT+1);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response2 = client.execute(request);



        assertEquals(404,response2.getStatusLine().getStatusCode() );


    }

    @Test
    public void testGetDisc() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", TITLE);
        jsonObject.put("barcode", EAN);
        jsonObject.put("director", NAME);
        jsonObject.put("fsk", 16);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URL_DISCS);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        client.execute(httpPost);


        HttpGet request = new HttpGet(URL_DISCS + EAN);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response2 = client.execute(request);

        if (response2.getStatusLine().getStatusCode() == 200) {

            JSONObject wantedDisc = new JSONObject(EntityUtils.toString(response2.getEntity()));
            assertEquals(jsonObject.toString(), wantedDisc.toString());
        }


    }


    @Test
    public void testGetNonExistentDisc() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", TITLE);
        jsonObject.put("barcode", EAN);
        jsonObject.put("director", NAME);
        jsonObject.put("fsk", 16);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URL_DISCS);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        client.execute(httpPost);


        HttpGet request = new HttpGet(URL_DISCS + EAN+1);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response2 = client.execute(request);


        assertEquals(404, response2.getStatusLine().getStatusCode());



    }


}
