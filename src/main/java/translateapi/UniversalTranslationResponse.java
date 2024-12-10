package translateapi;

import lombok.*;

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

