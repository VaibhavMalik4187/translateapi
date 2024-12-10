package translateapi;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonObject;
import org.json.JSONObject;
import space.dynomake.libretranslate.Language;

public class LibreTranslate {
    public static final String API_URL = "http://localhost:5000/translate";

    public static UniversalTranslationResponse libreTranslate(String text, String sourceLang, String targetLang) {
        try {
            JsonObject payload = new JsonObject();
            payload.addProperty("q", text);
            payload.addProperty("source", sourceLang);
            payload.addProperty("target", targetLang);
            // payload.addProperty("format", "text");
            payload.addProperty("format", "html");
            payload.addProperty("alternatives", 0);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            long startTime = System.currentTimeMillis(); // Start timing
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            long endTime = System.currentTimeMillis(); // End timing
            JSONObject jsonObject= new JSONObject(response.body());
            String translatedText = jsonObject.getString("translatedText");

            UniversalTranslationResponse resp = new UniversalTranslationResponse(sourceLang, targetLang, text, translatedText, (endTime - startTime), text.length());

            /*
            // Print the response and latency
            System.out.println("Original text: " + text);
            System.out.println("Response: " + response.body());
            System.out.println("Text length: " + text.length());
            System.out.println("Latency: " + (endTime - startTime) + " ms");
             */
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return new UniversalTranslationResponse();
        }
    }

    public static void main(String[] args) {
        System.out.println("Length: " + TextExamples.longest);
        System.out.println("Time: " + libreTranslate(TextExamples.longest, "auto", Language.ENGLISH.getCode()));
        // ReadTranslationData.read("/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/final-data.json");
        // libreTranslate(TextExamples.uiText, "auto", Language.KOREAN.getCode());
        /*
        for (String text: TextExamples.similarTexts) {
            libreTranslate(text, "auto", Language.ENGLISH.getCode());
        }
        for (int i = 0; i < 100; i++) {
            libreTranslate(TextExamples.veryLong, Language.SPANISH.getCode(), Language.ENGLISH.getCode());
        }
         */
        /*
        for (int i = 0; i < 100; i++) {
            libreTranslate(TextExamples.latestStr, Language.GERMAN.getCode(), Language.ENGLISH.getCode());
        }
         */
        // translateJSONData("/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/german-data.json", Language.GERMAN.getCode(), Language.ENGLISH.getCode(), 500, 1200);
        // translateJSONData("/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/jeopardy-questions.json", Language.ENGLISH.getCode(), Language.HINDI.getCode(), 0, 800);
        // translateJSONData("/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/german-data.json", Language.GERMAN.getCode(), Language.HINDI.getCode(), 500, 1200);
    }
}
