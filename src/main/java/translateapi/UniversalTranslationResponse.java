package translateapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.*;

import java.util.Arrays;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class UniversalTranslationResponse {
    String sourceLanguage;
    String targetLanguage;
    String sourceText;
    String translatedText;
    long latency;
    int textLength;
}

