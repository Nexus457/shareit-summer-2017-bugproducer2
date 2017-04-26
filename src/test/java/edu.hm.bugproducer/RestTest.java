package edu.hm.bugproducer;

import edu.hm.JettyStarter;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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



    @Before
    public void openConnection() throws Exception {
        JettyStarter.startJetty();
    }

    @After
    public void closeConnection() throws Exception {
        JettyStarter.stopJetty();
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
        jsonObject.put("isbn", ISBN);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URL_BOOKS);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(httpPost);
        System.out.println("testCreateBook: ");
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
        assertEquals(200,response.getStatusLine().getStatusCode());

        /*BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }*/

    }
    @Test
    public void testCreateDisc() throws IOException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", TITLE);
        jsonObject.put("barcode", NAME);
        jsonObject.put("director", NAME);
        jsonObject.put("fsk","16");

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
        assertEquals(400,response.getStatusLine().getStatusCode());
    }
    @Test
    public void testCreateDiscEmpty() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "");
        jsonObject.put("barcode", "");
        jsonObject.put("director", "");
        jsonObject.put("fsk","");

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URL_DISCS);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(httpPost);
        System.out.println("testCreateDiscEmpty: ");
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
        assertEquals(400,response.getStatusLine().getStatusCode());
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

        assertEquals(400,response1.getStatusLine().getStatusCode());
    }
    @Test
    public void testCreateDiscDuplicate() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", TITLE);
        jsonObject.put("barcode",EAN);
        jsonObject.put("director", NAME);
        jsonObject.put("fsk","16");

        JSONObject jsonObject1 = new JSONObject();
        jsonObject.put("title", TITLE);
        jsonObject.put("barcode", EAN);
        jsonObject.put("director", NAME);
        jsonObject.put("fsk","16");

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

        assertEquals(400,response1.getStatusLine().getStatusCode());
    }

}
