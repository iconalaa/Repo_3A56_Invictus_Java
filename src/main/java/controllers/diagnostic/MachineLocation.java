package tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

public class MachineLocation {
    public static JSONObject getMachineLocation() {
        try {
            URL url = new URL("https://ipinfo.io/json");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

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

    public static String getCityName(double latitude, double longitude) {
        try {
            URL url = new URL("https://nominatim.openstreetmap.org/reverse?format=json&lat=" + latitude + "&lon=" + longitude + "&accept-language=en");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            JSONObject addressObject = jsonObject.getJSONObject("address");
            return addressObject.getString("city");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    public static void main(String[] args) {
        JSONObject locationInfo = getMachineLocation();
        if (locationInfo != null) {
            try {
                String[] loc = locationInfo.getString("loc").split(",");
                double latitude = Double.parseDouble(loc[0]);
                double longitude = Double.parseDouble(loc[1]);
                System.out.println("Latitude: " + latitude);
                System.out.println("Longitude: " + longitude);

                String cityName = getCityName(latitude, longitude);
                System.out.println("City Name: " + cityName);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to retrieve machine location.");
            }
        } else {
            System.out.println("Failed to retrieve machine location.");
        }
    }
}
