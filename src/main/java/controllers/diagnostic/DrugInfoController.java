package controllers.diagnostic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class DrugInfoController {

    private static final String API_URL = "https://drug-info-and-price-history.p.rapidapi.com/1/druginfo";
    private static final String API_KEY = "f1d05417e6msh5913861b8c12ac2p1fffdajsn05d61070e277";

    public static JSONObject getDrugInfo(String drug) {
        try {
            URL url = new URL(API_URL + "?drug=" + drug);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-RapidAPI-Key", API_KEY);
            con.setRequestProperty("X-RapidAPI-Host", "drug-info-and-price-history.p.rapidapi.com");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return new JSONObject(response.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        JSONObject drugInfo = getDrugInfo("aspirin");
        if (drugInfo != null) {
            try {
                System.out.println("Drug Info: " + drugInfo.toString());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to retrieve drug information.");
            }
        } else {
            System.out.println("Failed to retrieve drug information.");
        }
    }
}
