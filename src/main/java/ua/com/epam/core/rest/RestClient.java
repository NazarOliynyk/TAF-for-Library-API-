package ua.com.epam.core.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minidev.json.JSONObject;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.groovy.runtime.metaclass.MetaMethodIndex;
import org.testng.Assert;
import ua.com.epam.entity.Response;
import ua.com.epam.utils.helpers.LocalDateAdapter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static ua.com.epam.config.URI.AUTHENTICATION_URI;
import static ua.com.epam.config.URI.BASE_URI;
import static ua.com.epam.utils.JsonKeys.AUTH_PASSWORD;
import static ua.com.epam.utils.JsonKeys.AUTH_USERNAME;

public class RestClient {

    //in the constructor- get authenticated before running all tests
    public RestClient() {
        this.authenticate(AUTHENTICATION_URI);
    }

    private String authorization = "";

    private static Logger log = Logger.getLogger(RestClient.class);

    //custom Response object (wrapper)
    private Response response;

    //build here our HTTPClient to execute needed requests
    private HttpClient client = HttpClientBuilder.create().build();

    //JSON parser with right Date format parsing specifying
    private Gson g = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();



    public Response getResponse() {
        return this.response;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    //POST authorization json
    public void authenticate(String uri) {

        HttpPost request = new HttpPost(BASE_URI + uri);
        JSONObject json = new JSONObject();
        json.put("username", AUTH_USERNAME);
        json.put("password", AUTH_PASSWORD);
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        try {
            StringEntity bodyToPost= new StringEntity(json.toString());
            request.setEntity(bodyToPost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpResponse response = null;
        try {
            log.info("Perform POST auth request to: " + request.getURI().toString());
            response = client.execute(request); //execute request and write response
            String header = String.valueOf(response.getFirstHeader("Authorization"));
            authorization = header.substring(15);
        } catch (ClientProtocolException e) {
            log.error("HTTP protocol error!");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Some problems occur or the connection was aborted!");
            e.printStackTrace();
        }

        wrapResponse(response);
    }

    //GET just take only URI as String
    public void get(String uri) {
        //create exactly GET http request. It assume String with URI
        HttpGet request = new HttpGet(BASE_URI + uri);

        //set header
        //here we say to API, that we expect especially JSON in response
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.addHeader("Authorization", authorization );
        //here we create HttpResponse object
        HttpResponse response = null;
        try {
            log.info("Perform GET request to: " + request.getURI().toString());
            response = client.execute(request); //execute request and write response
        } catch (ClientProtocolException e) {
            log.error("HTTP protocol error!");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Some problems occur or the connection was aborted!");
            e.printStackTrace();
        }

        //and the last: wrap HttpResponse to our custom Response object (look at line 148)
        wrapResponse(response);
    }

    //POST take URI but as String and object to post
    public void post(String uri, Object body) {
        HttpPost request = new HttpPost(BASE_URI + uri);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        request.addHeader("Authorization", authorization);
        String reqB = g.toJson(body);

        HttpResponse response = null;
        try {
            StringEntity bodyToPost = new StringEntity(reqB);
            request.setEntity(bodyToPost);

            log.info("Perform POST request to: " + request.getURI().toString());
            response = client.execute(request);


        } catch (UnsupportedEncodingException e) {
            log.error("The Character Encoding is not supported!");
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            log.error("HTTP protocol error!");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Some problems occur or the connection was aborted!");
            e.printStackTrace();
        }

        wrapResponse(response);
    }

    //PUT take URI but as String and object to put
    public void put(String uri, Object body) {
        HttpPut request = new HttpPut(BASE_URI + uri);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        request.addHeader("Authorization", authorization );
        String reqB = g.toJson(body);

        HttpResponse response = null;
        try {
            StringEntity bodyToPut = new StringEntity(reqB);
            request.setEntity(bodyToPut);

            log.info("Perform PUT request to: " + request.getURI().toString());
            response = client.execute(request);
        } catch (UnsupportedEncodingException e) {
            log.error("The character encoding is not supported!");
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            log.error("HTTP protocol error!");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Some problems occur or the connection was aborted!");
            e.printStackTrace();
        }

        wrapResponse(response);
    }

    //DELETE take URI as URIBuilder object
    public void delete(String uri) {
        HttpDelete request = new HttpDelete(BASE_URI + uri);
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.addHeader("Authorization", authorization );
        HttpResponse response = null;

        try {
            log.info("Perform DELETE request to: " + request.getURI().toString());
            response = client.execute(request);
        } catch (ClientProtocolException e) {
            log.error("HTTP protocol error!");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Some problems occur or the connection was aborted!");
            e.printStackTrace();
        }

        wrapResponse(response);
    }

    //response wrapper
    // here we check if we not catch some exceptions while colling API
    private void wrapResponse(HttpResponse response) {
        // just verify our response on null value
        if (response == null) {
            Assert.fail("Response is empty!");
        }

        //get status code from our HttpResponse object
        int statusCode = response.getStatusLine().getStatusCode();

        //and also read our response body
        HttpEntity entity = response.getEntity();

        //check if response body is not null (e. g. after delete )
        if (entity == null) {
            //will write response to custom object Response (inside RestClient object)
            this.response = new Response(statusCode, "");
            return;
        }

        String body = "";

        try {
            body = EntityUtils.toString(entity, "UTF-8");
        } catch (ParseException e) {
            log.error("Header elements cannot be parsed!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.response = new Response(statusCode, body);
    }
}
