package benchmarking;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GoogleV2Response {
    private String sourceLanguage;
    private String targetLanguage;
    private String requestBody;
    private String responseBody;
}
