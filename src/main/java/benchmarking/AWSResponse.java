package benchmarking;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AWSResponse {
    private String sourceLanguage;
    private String targetLanguage;
    private String sourcetext;
    private String translatedText;
    private long latency;
    private int textLength;
}
