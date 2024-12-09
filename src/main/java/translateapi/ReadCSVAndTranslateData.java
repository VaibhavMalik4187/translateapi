package translateapi;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.FileReader;
import java.io.IOException;
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
    public static void main(String[] args) {
        Map<Pair<String, String>, Integer> langFrequencyMap = new HashMap<>();
        String csvFile = "/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/filtered-data.csv";
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] nextLine;
            int counter = 0;
            while ((nextLine = reader.readNext()) != null) {
                counter++;
                // nextLine[] is an array of values from the line
                String sourceLanguage = nextLine[0];
                String targetLanguage = nextLine[1];
                String requestBody = nextLine[2];
                String responseBody = nextLine[3];

                System.out.println("Source Language: " + sourceLanguage);
                System.out.println("Target Language: " + targetLanguage);
                System.out.println("Request Body: " + requestBody);
                System.out.println("Response Body: " + responseBody);
                langFrequencyMap.put(new Pair<>(sourceLanguage, targetLanguage), langFrequencyMap.getOrDefault(new Pair<>(sourceLanguage, targetLanguage), 0) + 1);
                System.out.println("------------------------");
            }
            // Sort the map by values (frequencies) in descending order
            List<Map.Entry<Pair<String, String>, Integer>> entries = new ArrayList<>(langFrequencyMap.entrySet());
            entries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            System.out.println("Source target lang frequencies:");
            System.out.println("Total requests analyzed: " + counter);
            System.out.println(entries);

            // Print the sorted frequencies
            /*
            for (Map.Entry<Pair<String, String>, Integer> entry : entries) {
                System.out.println(entry.getKey().first + " -> " + entry.getKey().second + ": " + entry.getValue());
            }
             */
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }   System.out.printf("Hello");
    }

    public static void generateGraph(ArrayList<Long> latencies, ArrayList<Integer> textLengths, int minLength, int maxLength) {
        long totalLatency = 0;

        for (long latency: latencies) {
            totalLatency += latency;
        }

        int totalLength = 0;
        for (int length: textLengths) {
            totalLength += length;
        }

        double latencyPerChar = totalLatency / (double) totalLength;
        System.out.println("Average time taken per character: " + latencyPerChar + " ms");

        // Create an XYSeries to store the data
        XYSeries series = new XYSeries("Latency vs. String Length for min length: " + minLength + ", max length: " + maxLength + ", Average latency per character: " + latencyPerChar);
        for (int i = 0; i < textLengths.size(); i++) {
            series.add(textLengths.get(i), latencies.get(i));
        }

        // Create a dataset
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        // Create the chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Latency vs. String Length for min length: " + minLength + ", max length: " + maxLength + ", Average latency per character: " + latencyPerChar,
                "String Length",
                "Latency (ms)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Display the chart in a frame
        ChartFrame frame = new ChartFrame("Latency Plot", chart);
        frame.pack();
        frame.setVisible(true);
    }
}

