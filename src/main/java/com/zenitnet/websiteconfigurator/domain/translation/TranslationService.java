package com.zenitnet.websiteconfigurator.domain.translation;

import com.zenitnet.websiteconfigurator.common.exception.TechnicalException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@CommonsLog
@Service
public class TranslationService {

    @Value("${translation.deepl.api-key:}")
    private String apiKey;

    @Value("${translation.deepl.api-url:https://api-free.deepl.com/v2/translate}")
    private String apiUrl;

    @Value("#{'${translation.target-languages:EN,ES,HU}'.split(',')}")
    private List<String> targetLanguages;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<String> getTargetLanguages() {
        return targetLanguages;
    }

    public TranslationResult translate(String text, String targetLanguage, String sourceLanguage) {
        if (apiKey == null || apiKey.isBlank()) {
            return TranslationResult.failure("Translation API key is not configured");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", "DeepL-Auth-Key " + apiKey);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("text", text);
            body.add("source_lang", sourceLanguage.toUpperCase());
            body.add("target_lang", targetLanguage.toUpperCase());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, request, Map.class);

            if (response.getBody() != null) {
                List<Map<String, String>> translations = (List<Map<String, String>>) response.getBody().get("translations");
                if (translations != null && !translations.isEmpty()) {
                    return TranslationResult.success(translations.get(0).get("text"));
                }
            }

            return TranslationResult.failure("Empty response from translation service");

        } catch (HttpClientErrorException.TooManyRequests e) {
            log.warn("DeepL rate limit exceeded");
            return TranslationResult.failure("Translation rate limit exceeded. Please try again later.");
        } catch (HttpClientErrorException.Forbidden e) {
            log.warn("DeepL API key invalid or quota exceeded");
            return TranslationResult.failure("Translation API quota exceeded or key invalid.");
        } catch (HttpClientErrorException.BadRequest e) {
            log.warn("DeepL bad request for language: " + targetLanguage);
            return TranslationResult.failure("Language '" + targetLanguage + "' is not supported by the translation service.");
        } catch (Exception e) {
            log.error("Translation failed for target language: " + targetLanguage, e);
            return TranslationResult.failure("Translation failed for language '" + targetLanguage + "': " + e.getMessage());
        }
    }
}
