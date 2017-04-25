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
        String url = "http://localhost:8082";

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

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

        String url = "http://localhost:8082/shareit/media/books/";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "testtitle");
        jsonObject.put("author", "testAuthor");
        jsonObject.put("isbn", "123");

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(httpPost);
        System.out.println("testCreateBook: ");
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
        assertEquals("200",response.getStatusLine().getStatusCode());

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
        String url = "http://localhost:8082/shareit/media/discs/";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "testtitle");
        jsonObject.put("barcode", "123");
        jsonObject.put("director", "testname");
        jsonObject.put("fsk","16");

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(httpPost);
        System.out.println("testCreateDisc: ");
        System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
        assertEquals("200", response.getStatusLine().getStatusCode());
    }
    @Test
    public void testCreateBookEmpty() throws IOException {
        String url = "http://localhost:8082/shareit/media/books/";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "");
        jsonObject.put("author", "");
        jsonObject.put("isbn", "");

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        httpPost.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(httpPost);
        System.out.println("testCreateBookEmpty: ");
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
        assertEquals("400",response.getStatusLine().getStatusCode());
    }
}
