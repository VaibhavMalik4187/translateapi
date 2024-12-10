package translateapi;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.tomcat.util.json.JSONParser;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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

public class ReadCSVAndTranslateData {
    public static final String googleTranslateResponsesFile = "/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/google-v2-responses.json";

    public static List<Pair<String, String>> getSampleTexts(String filePath) {
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
        /*
        for (Pair<String, String> textLanguagePair: getSampleTexts(googleTranslateResponsesFile)) {
            System.out.println(textLanguagePair);
            System.out.println("----------------");
        }
         */
    }
}