package translateapi;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonObject;

public class LibreTranslate {
    public static final String API_URL = "http://localhost:5000/translate";

    public static void libreTranslate(String text, String sourceLang, String targetLang) {
        try {
            JsonObject payload = new JsonObject();
            payload.addProperty("q", text);
            payload.addProperty("source", sourceLang);
            payload.addProperty("target", targetLang);
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
            System.out.println("Latency: " + (endTime - startTime) / 1_000_000 + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        libreTranslate(TextExamples.largeString, "auto", "hi");

        for (String text: TextExamples.hindiTexts) {
            libreTranslate(text, "auto", "en");
        }

        for (TextExamples.TextExample text: TextExamples.TextExample.values()) {
            libreTranslate(text.getText(), "auto", "hi");
        }
    }
}
