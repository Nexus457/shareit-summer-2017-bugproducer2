package edu.hm.bugproducer;

import edu.hm.JettyStarter;
import edu.hm.bugproducer.restAPI.media.MediaServiceImpl;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpHeaders.USER_AGENT;
import static org.junit.Assert.assertEquals;

public class RestTest {

    private static final String USER_NAME = "JOh";
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
    private static final String URL_COPYS_BOOKS = "http://localhost:8082/shareit/copy/books";
    private static final String URL_COPYS_DISCS = "http://localhost:8082/shareit/copy/discs/";

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
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        HttpPost httpPost1 = new HttpPost(URL_BOOKS);
        httpPost1.setEntity(new StringEntity(jsonObject1.toString()));
        httpPost1.addHeader("content-Type", "application/json");
        HttpResponse response1 = client.execute(httpPost1);
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
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        HttpPost httpPost1 = new HttpPost(URL_BOOKS);
        httpPost1.setEntity(new StringEntity(jsonObject1.toString()));
        httpPost1.addHeader("content-Type", "application/json");
        HttpResponse response1 = client.execute(httpPost1);
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

    @Test
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


        HttpGet request = new HttpGet(URL_BOOKS + ISBN_ALT + 1);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response2 = client.execute(request);
        assertEquals(404, response2.getStatusLine().getStatusCode());


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

        HttpGet request = new HttpGet(URL_DISCS + EAN + 1);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response2 = client.execute(request);
        assertEquals(404, response2.getStatusLine().getStatusCode());
    }

    @Test
    public void testUpdateBook() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", TITLE);
        jsonObject.put("author", NAME);
        jsonObject.put("isbn", ISBN_ALT);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URL_BOOKS);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(httpPost);
        assertEquals(200, response.getStatusLine().getStatusCode());

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("title", TITLE_ALT);

        HttpPut httpPut = new HttpPut(URL_BOOKS + ISBN_ALT);
        httpPut.setEntity(new StringEntity(jsonObject2.toString()));
        httpPut.addHeader("content-Type", "application/json");
        HttpResponse response2 = client.execute(httpPut);
        System.out.println("Response Code : " + response2.getStatusLine().getStatusCode());
        assertEquals(200, response2.getStatusLine().getStatusCode());
    }

    @Test
    public void testUpdateNonExistentBook() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", TITLE);
        jsonObject.put("author", NAME);
        jsonObject.put("isbn", ISBN_ALT);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URL_BOOKS);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(httpPost);
        assertEquals(200, response.getStatusLine().getStatusCode());

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("title", TITLE_ALT);

        HttpPut httpPut = new HttpPut(URL_BOOKS + ISBN);
        httpPut.setEntity(new StringEntity(jsonObject2.toString()));
        httpPut.addHeader("content-Type", "application/json");
        HttpResponse response2 = client.execute(httpPut);
        System.out.println("Response Code : " + response2.getStatusLine().getStatusCode());
        assertEquals(400, response2.getStatusLine().getStatusCode());
    }

    @Test
    public void testUpdateDisc() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", TITLE);
        jsonObject.put("director", NAME);
        jsonObject.put("barcode", EAN);
        jsonObject.put("fsk", 16);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URL_DISCS);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(httpPost);
        assertEquals(200, response.getStatusLine().getStatusCode());

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("title", TITLE_ALT);
        jsonObject2.put("fsk", 12);

        HttpPut httpPut = new HttpPut(URL_DISCS + EAN);
        httpPut.setEntity(new StringEntity(jsonObject2.toString()));
        httpPut.addHeader("content-Type", "application/json");
        HttpResponse response2 = client.execute(httpPut);

        System.out.println("Response Code : " + response2.getStatusLine().getStatusCode());
        assertEquals(200, response2.getStatusLine().getStatusCode());
    }

    @Test
    public void testUpdateNonExistingDisc() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", TITLE_ALT);
        jsonObject.put("director", NAME_ALT);
        jsonObject.put("barcode", EAN_ALT);
        jsonObject.put("fsk", 0);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URL_DISCS);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(httpPost);
        assertEquals(200, response.getStatusLine().getStatusCode());

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("title", TITLE);
        jsonObject2.put("fsk", 12);

        HttpPut httpPut = new HttpPut(URL_DISCS + EAN);
        httpPut.setEntity(new StringEntity(jsonObject2.toString()));
        httpPut.addHeader("content-Type", "application/json");
        HttpResponse response2 = client.execute(httpPut);

        System.out.println("Response Code : " + response2.getStatusLine().getStatusCode());
        assertEquals(400, response2.getStatusLine().getStatusCode());
    }

    @Test
    public void testUpdateBookWrongISBN() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", TITLE);
        jsonObject.put("author", NAME);
        jsonObject.put("isbn", ISBN);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URL_BOOKS);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(httpPost);
        assertEquals(200, response.getStatusLine().getStatusCode());

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("isbn", "123");

        HttpPut httpPut = new HttpPut(URL_BOOKS + ISBN);
        httpPut.setEntity(new StringEntity(jsonObject2.toString()));
        httpPut.addHeader("content-Type", "application/json");
        HttpResponse response2 = client.execute(httpPut);
        System.out.println("Response Code : " + response2.getStatusLine().getStatusCode());
        assertEquals(400, response2.getStatusLine().getStatusCode());
    }

    @Test
    public void testUpdateDiscWrongEAN() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", TITLE);
        jsonObject.put("director", NAME);
        jsonObject.put("barcode", EAN);
        jsonObject.put("fsk", 16);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URL_DISCS);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(httpPost);
        assertEquals(200, response.getStatusLine().getStatusCode());

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("barcode", "12345");

        HttpPut httpPut = new HttpPut(URL_DISCS + EAN);
        httpPut.setEntity(new StringEntity(jsonObject2.toString()));
        httpPut.addHeader("content-Type", "application/json");
        HttpResponse response2 = client.execute(httpPut);

        System.out.println("Response Code : " + response2.getStatusLine().getStatusCode());
        assertEquals(400, response2.getStatusLine().getStatusCode());
    }

    @Test
    public void testCreateCopy() throws IOException {
        List<NameValuePair> nameValuePairs = new ArrayList<>();

        JSONObject book = new JSONObject();
        book.put("title", TITLE);
        book.put("author", NAME);
        book.put("isbn", ISBN);


        nameValuePairs.add(new BasicNameValuePair("user", "Joh"));
        nameValuePairs.add(new BasicNameValuePair("code", ISBN));
        //nameValuePairs.add(new BasicNameValuePair("lfnr", "1"));


        HttpClient client = HttpClientBuilder.create().build();
        HttpPost addBook = new HttpPost(URL_BOOKS);
        HttpPost addCopyBook = new HttpPost(URL_COPYS_BOOKS);
        addBook.setEntity(new StringEntity(book.toString()));


        addCopyBook.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        addBook.addHeader("content-Type", "application/json");
        addCopyBook.addHeader("content-Type", "application/x-www-form-urlencoded");
        client.execute(addBook);
        HttpResponse response = client.execute(addCopyBook);
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

}
