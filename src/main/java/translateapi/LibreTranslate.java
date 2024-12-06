package translateapi;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonObject;
import space.dynomake.libretranslate.Language;

import java.io.FileReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

public class LibreTranslate {
    public static final String API_URL = "http://localhost:5000/translate";

    public static void libreTranslate(String text, String sourceLang, String targetLang) {
        try {
            JsonObject payload = new JsonObject();
            payload.addProperty("q", text);
            payload.addProperty("source", sourceLang);
            payload.addProperty("target", targetLang);
            // payload.addProperty("format", "text");
            payload.addProperty("format", "text");
            payload.addProperty("alternatives", 0);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            long startTime = System.nanoTime(); // Start timing
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            long endTime = System.nanoTime(); // End timing

            // Print the response and latency
            System.out.println("Response: " + response.body());
            System.out.println("Text length: " + text.length());
            System.out.println("Latency: " + (endTime - startTime) / 1_000_000 + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void translateJSONData(String filePath, String sourceLang, String targetLang) {
        try (FileReader reader = new FileReader(filePath)) {
            JSONArray jsonArray = new JSONArray(new JSONTokener(reader));

            for (int i = 0; i < jsonArray.length(); i++) {
                String text = jsonArray.getString(i);

                if (text.length() < 5000) {
                    // Process each text here
                    libreTranslate(text, sourceLang, targetLang);
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // translateJSONData("/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/german-data.json", Language.GERMAN.getCode(), Language.ENGLISH.getCode());
        translateJSONData("/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/jeopardy-questions.json", Language.ENGLISH.getCode(), Language.HINDI.getCode());
    }
}
