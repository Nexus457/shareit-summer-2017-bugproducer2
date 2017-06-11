package edu.hm.bugproducer;




import edu.hm.JettyStarter;
import edu.hm.bugproducer.models.Disc;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("Duplicates")
public class RestTestDiscWithMock {

    private static final String NAME = "TestName1";
    private static final String NAME_ALT = "TestName2";
    private static final String TITLE = "TestTitle1";
    private static final String TITLE_ALT = "TestTitle2";
    private static final String ISBN = "3-446-193138";
    private static final String URL = "http://localhost:8082";
    private static final String EAN = "9783815820865";
    private static final String EAN_ALT = "9783827317100";
    private static final String INVALID_EAN = "1234";
    private static final int FSK = 6;
    private static final int FSK_ALT = 18;

    private static final String WRONG_ISBN = "0-7475006";
    private static final String WRONG_EAN = "3-446-19313";

    private static final String URL_BOOKS = "http://localhost:8080/shareit/media/books/";
    private static final String URL_DISCS = "http://localhost:8080/shareit/media/discs/";

    private JettyStarter jettyStarter;

    List<Disc> emptyDiscList = new ArrayList<>();
    Disc normalDisc = new Disc(NAME, EAN, TITLE, FSK);
    Disc emptyDisc = new Disc("", "", "", -1);
    Disc invalidEANDisc = new Disc(NAME, INVALID_EAN, TITLE, FSK);
    Disc duplicateDisc = new Disc(NAME_ALT, EAN, TITLE, FSK);
    Disc updateDisc = new Disc(NAME, "", TITLE, FSK);
    Disc emptyUpdateDisc = new Disc("", "", "", FSK);

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
    public void testAddDisc() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();

        JSONObject disc = new JSONObject();
        disc.put("title", TITLE);
        disc.put("director", NAME);
        disc.put("barcode", EAN);
        disc.put("fsk", FSK);

        Map<String, Object> headerClaims = new HashMap();
        headerClaims.put("type", Header.JWT_TYPE);
        String compactJws = null;

        try {
            compactJws = Jwts.builder()
                    .claim("disc", disc.toString())
                    .setHeader(headerClaims)
                    .signWith(SignatureAlgorithm.HS256, "secret".getBytes("UTF-8"))
                    .compact();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpPost addDisc = new HttpPost(URL_DISCS);
        addDisc.setEntity(new StringEntity(compactJws));
        addDisc.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(addDisc);
        assertEquals("{\"result\":\"MSR_OK\",\"msg\":\"ok\",\"code\":200}", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testAddEmptyDisc() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();

        JSONObject disc = new JSONObject();
        disc.put("title", "");
        disc.put("director", "");
        disc.put("barcode", "");
        disc.put("fsk", -1);

        Map<String, Object> headerClaims = new HashMap();
        headerClaims.put("type", Header.JWT_TYPE);
        String compactJws = null;

        try {
            compactJws = Jwts.builder()
                    .claim("disc", disc.toString())
                    .setHeader(headerClaims)
                    .signWith(SignatureAlgorithm.HS256, "secret".getBytes("UTF-8"))
                    .compact();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpPost addDisc = new HttpPost(URL_DISCS);
        addDisc.setEntity(new StringEntity(compactJws));
        addDisc.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(addDisc);
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testAddDiscInvalidEAN() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();

        JSONObject disc = new JSONObject();
        disc.put("title", TITLE);
        disc.put("director", NAME);
        disc.put("barcode", INVALID_EAN);
        disc.put("fsk", FSK);

        Map<String, Object> headerClaims = new HashMap();
        headerClaims.put("type", Header.JWT_TYPE);
        String compactJws = null;

        try {
            compactJws = Jwts.builder()
                    .claim("disc", disc.toString())
                    .setHeader(headerClaims)
                    .signWith(SignatureAlgorithm.HS256, "secret".getBytes("UTF-8"))
                    .compact();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpPost addDisc = new HttpPost(URL_DISCS);
        addDisc.setEntity(new StringEntity(compactJws));
        addDisc.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(addDisc);
        assertEquals("{\"result\":\"MSR_BAD_REQUEST\",\"msg\":\"Barcode was not valid\",\"code\":400}", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testAddDiscDuplicate() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();

        JSONObject disc = new JSONObject();
        disc.put("title", TITLE_ALT);
        disc.put("director", NAME);
        disc.put("barcode", EAN);
        disc.put("fsk", FSK);

        Map<String, Object> headerClaims = new HashMap();
        headerClaims.put("type", Header.JWT_TYPE);
        String compactJws = null;

        try {
            compactJws = Jwts.builder()
                    .claim("disc", disc.toString())
                    .setHeader(headerClaims)
                    .signWith(SignatureAlgorithm.HS256, "secret".getBytes("UTF-8"))
                    .compact();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpPost addDisc = new HttpPost(URL_DISCS);
        addDisc.setEntity(new StringEntity(compactJws));
        addDisc.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(addDisc);
        assertEquals("{\"result\":\"MSR_BAD_REQUEST\",\"msg\":\"The disc is already in the system. No duplicate allowed\",\"code\":400}", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testGetDiscs() throws IOException {

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL_DISCS);
        HttpResponse response = client.execute(request);
        assertEquals("[]", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testGetDisc() throws IOException{
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL_DISCS + EAN);
        HttpResponse response = client.execute(request);
        assertEquals("{\"title\":\"TestTitle1\",\"barcode\":\"9783815820865\",\"director\":\"TestName1\",\"fsk\":6}", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testGetDiscNonExisting() throws IOException{
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL_DISCS + EAN_ALT);
        HttpResponse response = client.execute(request);
        assertEquals("{\"result\":\"MSR_NOT_FOUND\",\"msg\":\"The disc you have searched for is not in the system!\",\"code\":404}", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testUpdateDisc() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();

        JSONObject disc = new JSONObject();
        disc.put("title", TITLE);
        disc.put("director", NAME);
        disc.put("barcode", "");
        disc.put("fsk", FSK);


        Map<String, Object> headerClaims = new HashMap();
        headerClaims.put("type", Header.JWT_TYPE);
        String compactJws = null;

        try {
            compactJws = Jwts.builder()
                    .claim("disc", disc.toString())
                    .setHeader(headerClaims)
                    .signWith(SignatureAlgorithm.HS256, "secret".getBytes("UTF-8"))
                    .compact();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpPut updateDisc = new HttpPut(URL_DISCS + EAN);
        updateDisc.setEntity(new StringEntity(compactJws));
        updateDisc.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(updateDisc);
        assertEquals("{\"result\":\"MSR_OK\",\"msg\":\"ok\",\"code\":200}", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testUpdateDiscEmpty() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();

        JSONObject disc = new JSONObject();
        disc.put("title", "");
        disc.put("director", "");
        disc.put("barcode", "");
        disc.put("fsk", -1);


        Map<String, Object> headerClaims = new HashMap();
        headerClaims.put("type", Header.JWT_TYPE);
        String compactJws = null;

        try {
            compactJws = Jwts.builder()
                    .claim("disc", disc.toString())
                    .setHeader(headerClaims)
                    .signWith(SignatureAlgorithm.HS256, "secret".getBytes("UTF-8"))
                    .compact();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpPut updateDisc = new HttpPut(URL_DISCS + EAN);
        updateDisc.setEntity(new StringEntity(compactJws));
        updateDisc.addHeader("content-Type", "application/json");
        HttpResponse response = client.execute(updateDisc);
        assertEquals("{\"result\":\"MSR_BAD_REQUEST\",\"msg\":\"Director, Title and FSK are empty!\",\"code\":400}", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testUpdateNonExistingDisc() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();

        JSONObject disc = new JSONObject();
        disc.put("title", TITLE);
        disc.put("director", NAME);
        disc.put("barcode", "");
        disc.put("fsk", FSK);


        Map<String, Object> headerClaims = new HashMap();
        headerClaims.put("type", Header.JWT_TYPE);
        String compactJws = null;

        try {
            compactJws = Jwts.builder()
                    .claim("disc", disc.toString())
                    .setHeader(headerClaims)
                    .signWith(SignatureAlgorithm.HS256, "secret".getBytes("UTF-8"))
                    .compact();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpPut updateDisc = new HttpPut(URL_DISCS + EAN_ALT);
        updateDisc.setEntity(new StringEntity(compactJws));
        updateDisc.addHeader("content-Type", "application/json");
        HttpResponse shareItResponse = client.execute(updateDisc);
        assertEquals("{\"result\":\"MSR_BAD_REQUEST\",\"msg\":\"The disc you want to update is not in the system!\",\"code\":400}", EntityUtils.toString(shareItResponse.getEntity()));
    }



}
