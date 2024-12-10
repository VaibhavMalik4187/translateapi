package translateapi;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.translate.TranslateClient;
import software.amazon.awssdk.services.translate.model.TranslateTextRequest;
import software.amazon.awssdk.services.translate.model.TranslateTextResponse;

public class AWSTranslate {
    public static final String ACCESS_KEY = System.getenv("AWS_ACCESS_KEY");
    public static final String SECRET_KEY = System.getenv("AWS_SECRET_KEY");

    public static UniversalTranslationResponse translate(String text, String sourceLang, String targetLang){
        try {
            AwsBasicCredentials awsCreds = AwsBasicCredentials.create(ACCESS_KEY , SECRET_KEY);
            TranslateClient translateClient = TranslateClient.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                    .region(Region.AP_SOUTH_1)
                    .build();
            TranslateTextRequest translateTextRequest = TranslateTextRequest.builder()
                    .text(text)
                    .sourceLanguageCode(sourceLang)
                    .targetLanguageCode(targetLang)
                    .build();
            long startTime = System.currentTimeMillis();
            TranslateTextResponse response = translateClient.translateText(translateTextRequest);
            long endTime = System.currentTimeMillis();

            UniversalTranslationResponse resp = new UniversalTranslationResponse(sourceLang, targetLang, text, response.translatedText(), (endTime - startTime), text.length());

            System.out.println("Length of string:- " + text.length() + "and " + "Time taken is:- " + (endTime - startTime));
            translateClient.close();
            return  resp;
        } catch (Exception e) {
            e.printStackTrace();
            return new UniversalTranslationResponse();
        }
    }

    public static void main(String[] args) {
        /*
        List<String> translationSentences = TranslationData.getTranslationSentences();
        List<String> awsResults = new ArrayList<>() ;
        for (String fact : translationSentences) {
            awsResults.add(translate(fact , "en"));
        }
        for (String fact : awsResults) {
            System.out.println(fact);
            System.out.println();
        }
         */
    }
}
