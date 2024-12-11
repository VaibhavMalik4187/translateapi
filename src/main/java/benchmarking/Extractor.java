package benchmarking;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Extractor {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        List<AWSResponse> awsResponses = parseAWSResponse();
        List<GoogleV2Response> googleV2Responses = parseGoogleResponse();
        String fullBenchmarkFilePath = "benchmark.csv";
        String lanWiseBenchmarkFilePath = "benchmark_lan.csv";

        List<String> fullBenchMarkContent = new ArrayList<>();
        List<String> lanWiseBenchMarkContent = new ArrayList<>();
        List<LatencyData> latencyDataList = new ArrayList<>();
        Map<String, Boolean> isDone = new HashMap<>();

        int size = awsResponses.size();
        for (int i = 0; i < size; i++) {
            String sourceLan = googleV2Responses.get(i).getSourceLanguage();
            long latency = awsResponses.get(i).getLatency();
            int textLength = awsResponses.get(i).getTextLength();
            String content = sourceLan + "," + textLength + "," + latency;
            latencyDataList.add(new LatencyData(sourceLan, latency, textLength));
            fullBenchMarkContent.add(content);
        }

        for (int i = 0; i < size; i++) {
            String sourceLan = latencyDataList.get(i).getSourceLan();
            int fre = 0;
            double latencyPerLen = 0;

            if (isDone.containsKey(sourceLan)) {
                continue;
            }

            for (int j = 0; j < size; j++) {
                LatencyData currentData = latencyDataList.get(j);
                String currSL = currentData.getSourceLan();

                if (currSL.equals(sourceLan)) {
                    latencyPerLen += (double) currentData.getLatency() / currentData.getTextLength();
                    fre++;
                }
            }
            latencyPerLen /= fre;
            String content = sourceLan + "," + fre + "," + latencyPerLen;
            lanWiseBenchMarkContent.add(content);
            isDone.put(sourceLan, true);
        }
        writeToCSVFile(fullBenchMarkContent, fullBenchmarkFilePath);
        writeToCSVFile(lanWiseBenchMarkContent, lanWiseBenchmarkFilePath);
    }

    private static List<AWSResponse> parseAWSResponse() {
        String filePath = "aws-responses.json";
        List<AWSResponse> awsResponses = new ArrayList<>();
        try {
            awsResponses = mapper.readValue(
                    new File(filePath),
                    new TypeReference<>() {
                    }
            );
            System.out.println("Total responses: " + awsResponses.size());
            System.out.println("sL: " + awsResponses.getFirst().getSourceLanguage());
            System.out.println("Latency: " + awsResponses.getFirst().getLatency());
            System.out.println("Text length: " + awsResponses.getFirst().getTextLength());

        } catch (Exception e) {
            System.out.println("Error while de-serialisation from file: " + filePath);
        }
        return awsResponses;
    }

    private static List<GoogleV2Response> parseGoogleResponse() {
        String filePath = "google-v2-responses.json";
        List<GoogleV2Response> googleV2Responses = new ArrayList<>();
        try {
            googleV2Responses = mapper.readValue(
                    new File(filePath),
                    new TypeReference<>() {
                    }
            );
            System.out.println("Total responses: " + googleV2Responses.size());
            System.out.println("sL: " + googleV2Responses.getFirst().getSourceLanguage());

        } catch (Exception e) {
            System.out.println("Error while de-serialisation from file: " + filePath);
        }
        return googleV2Responses;
    }

    private static void writeToCSVFile(List<String> csvContent, String filePath) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {
            for (String content : csvContent) {
                bufferedWriter.write(content);
                bufferedWriter.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error writing file: " + filePath);
        }
    }
}


/*
sL, avgLength, Latency
*
* */