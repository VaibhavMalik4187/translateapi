package translateapi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

class Pair<T, U> {
    private final T first;
    private final U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    // Getters
    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    // Overriding equals and hashCode for proper map functionality
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return getFirst() + ", " + getSecond();
    }
}

public class ReadAndTranslateData {
    public static final String googleTranslateResponsesFile = "/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/google-v2-responses.json";
    public static final String libreResponseFile = "/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/libre-responses.json";

    public static List<Pair<String, String>> getTextAndTargetLanguageSamples(String filePath) {
        List<Pair<String, String>> texts = new ArrayList<>();

        try {
            // Read JSON file as a String
            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));

            // Parse the JSON content into a JSONArray
            JSONArray jsonArray = new JSONArray(jsonContent);

            // Iterate through each JSON object in the array
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extract fields
                String targetLanguage = jsonObject.getString("targetLanguage");
                String text = jsonObject.getString("requestBody");
                texts.add(new Pair<>(text, targetLanguage));
            }
        } catch (Exception e) {
            System.err.println("Error reading the JSON file: " + e.getMessage());
        }
        return texts;
    }

    public static void main(String[] args) {
        ArrayList<UniversalTranslationResponse> responses = new ArrayList<>();
        int counter = 0;

        for (Pair<String, String> textLanguagePair: getTextAndTargetLanguageSamples(googleTranslateResponsesFile)) {
            System.out.println("Translating string #" + counter++);
            responses.add(LibreTranslate.libreTranslate(textLanguagePair.getFirst(), "auto", textLanguagePair.getSecond()));
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ConvertToJson.saveJsonToFile(responses, libreResponseFile);
    }
}