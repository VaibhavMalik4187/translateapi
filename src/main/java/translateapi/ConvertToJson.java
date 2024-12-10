package translateapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class ConvertToJson {
    public static ObjectNode[] convertUniversalTranslationResponseArrayToJson(UniversalTranslationResponse[] responses){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode[] jsonArray = new ObjectNode[responses.length];

        for (int i=0; i<responses.length; i++) {
            ObjectNode jsonObject = mapper.createObjectNode();
            jsonObject.put("sourceLanguage", responses[i].getSourceLanguage());
            jsonObject.put("targetLanguage", responses[i].getTargetLanguage());
            jsonObject.put("sourcetext", responses[i].getSourceText());
            jsonObject.put("translatedText", responses[i].getTranslatedText());
            jsonObject.put("latency", responses[i].getLatency());
            jsonObject.put("textLength", responses[i].getTextLength());
            jsonArray[i] = jsonObject;
        }

        System.out.println(Arrays.toString(jsonArray));

        return jsonArray;
    }

    public static void saveJsonToFile(ObjectNode[] jsonResult, String fileName) {
        ObjectMapper mapper = new ObjectMapper();

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonResult));
            System.out.println("JSON saved to " + fileName);
        } catch (IOException e) {
            System.err.println("Error while saving JSON to file: " + e.getMessage());
        }
    }
}
