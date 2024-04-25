package tests;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {

    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://endlessmedicalapi1.p.rapidapi.com/UpdateFeature?name=%3CREQUIRED%3E&value=%3CREQUIRED%3E&SessionID=%3CREQUIRED%3E"))
                .header("X-RapidAPI-Key", "f1d05417e6msh5913861b8c12ac2p1fffdajsn05d61070e277")
                .header("X-RapidAPI-Host", "endlessmedicalapi1.p.rapidapi.com")
                .method("POST", HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
