package translateapi;

import java.io.IOException;

import com.google.cloud.translate.*;
import com.google.cloud.translate.Translate.TranslateOption;

public class GoogleTranslateV2 {
    public static final String API_KEY = System.getenv("GOOGLE_API_KEY");

    public static Translate createTranslateService(String apiKey) {
        return TranslateOptions.newBuilder().setApiKey(apiKey).build().getService();
    }

    public static Detection translateText(Translate translate, String text) {
        Detection translation = translate.detect(text);
        return translation;
    }
    public static UniversalTranslationResponse translateText(Translate translate, String text, String targetLanguage) {
        long startTime = System.currentTimeMillis();
        Translation translation = translate.translate(text, TranslateOption.targetLanguage(targetLanguage));
        long endTime = System.currentTimeMillis();
        UniversalTranslationResponse  response = new UniversalTranslationResponse(
                "auto", targetLanguage ,
                text, translation.getTranslatedText(), (endTime - startTime), text.length()
        ) ;
        return response;
    }
}
