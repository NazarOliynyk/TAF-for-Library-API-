package ua.com.epam;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minidev.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.annotations.Test;
//import ua.com.epam.entity.ClientDataAuthModel;
import ua.com.epam.core.rest.RestClient;
import ua.com.epam.entity.author.Author;
import ua.com.epam.entity.author.nested.Name;
import ua.com.epam.utils.helpers.LocalDateAdapter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;

import static ua.com.epam.config.URI.BASE_URI;
import static ua.com.epam.utils.JsonKeys.AUTH_PASSWORD;
import static ua.com.epam.utils.JsonKeys.AUTH_USERNAME;
import ua.com.epam.core.rest.RestClient.*;

@Test(description = "Try authorization with default credentials")
public class SimpleAuthTest
       // extends BaseTest
{

    private Gson g = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

    private HttpClient client = HttpClientBuilder.create().build();

    private RestClient restClient = new RestClient();;

    @Test
    public void authenticate(){
        restClient.authenticate("/api/library/login");
        System.out.println(restClient.getAuthorization());
    }

    @Test
    public void authenticateWithJSON() throws IOException {

        JSONObject json = new JSONObject();
        json.put("username", AUTH_USERNAME);
        json.put("password", AUTH_PASSWORD);
        StringEntity bodyToPost= new StringEntity(json.toString());
        HttpPost request = new HttpPost(BASE_URI + "/api/library/login");
        request.setEntity(bodyToPost);

        HttpResponse response  = client.execute(request);

        String header = String.valueOf(response.getFirstHeader("Authorization"));
       // String bearer = header.split(" ")[1]+" "+header.split(" ")[2];
        String bearer = header.substring(15);
        System.out.println("bearer: "+bearer);
        restClient.setAuthorization(bearer);
        //restClient.setAuthorization("GJFJGFJHGFJK");
        //        restClient.AUTHORIZATION = String.valueOf(response.getFirstHeader("Authorization"));
        System.out.println(response.getEntity());
        System.out.println(response.getStatusLine().getStatusCode());
        System.out.println(response.getAllHeaders().length);
        System.out.println("restClient.getAuthorization(): "+restClient.getAuthorization());
//        System.out.println(response.getFirstHeader("Authorization"));
//        System.out.println(String.valueOf(response.getFirstHeader("Authorization")));

    }

    @Test
    public void saveAuthor() throws IOException {

        System.out.println("restClient.getAuthorization(): "+restClient.getAuthorization());
        Author author = new Author();
        author.setAuthorId((long) 25);
        author.setAuthorName(new Name("Nazik", "Nazik"));

        HttpPost request = new HttpPost(BASE_URI + "/api/library/author");
        String authorJSON = g.toJson(author);

        StringEntity bodyToPost = new StringEntity(authorJSON);

        request.setEntity(bodyToPost);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", restClient.getAuthorization());
        HttpResponse response  = client.execute(request);

        System.out.println(response.getEntity());
        System.out.println(response.getStatusLine().getStatusCode());
        System.out.println(response.getAllHeaders().length);
        //System.out.println(response.getFirstHeader("Authorization"));
    }

}
