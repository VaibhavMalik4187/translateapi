package translateapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ConvertToJson {
    public static ObjectNode[] convertUniversalTranslationResponseArrayToJson(ArrayList<UniversalTranslationResponse> responses){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode[] jsonArray = new ObjectNode[responses.size()];

        for (int i=0; i<responses.size(); i++) {
            ObjectNode jsonObject = mapper.createObjectNode();
            jsonObject.put("sourceLanguage", responses.get(i).getSourceLanguage());
            jsonObject.put("targetLanguage", responses.get(i).getTargetLanguage());
            jsonObject.put("sourcetext", responses.get(i).getSourceText());
            jsonObject.put("translatedText", responses.get(i).getTranslatedText());
            jsonObject.put("latency", responses.get(i).getLatency());
            jsonObject.put("textLength", responses.get(i).getTextLength());
            jsonArray[i] = jsonObject;
        }

        System.out.println(Arrays.toString(jsonArray));

        return jsonArray;
    }

    public static void saveJsonToFile(ArrayList<UniversalTranslationResponse> responses, String fileName) {
        ObjectNode[] jsonResult = convertUniversalTranslationResponseArrayToJson(responses);
        ObjectMapper mapper = new ObjectMapper();

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonResult));
            System.out.println("JSON saved to " + fileName);
        } catch (IOException e) {
            System.err.println("Error while saving JSON to file: " + e.getMessage());
        }
    }
}
