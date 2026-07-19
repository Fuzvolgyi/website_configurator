package com.zenitnet.websiteconfigurator.controller.rest;

import com.zenitnet.websiteconfigurator.domain.text.TextService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Public Texts", description = "Public endpoint for fetching text overrides")
@RequiredArgsConstructor
@RestController
@RequestMapping(PublicTextController.API_PATH)
public class PublicTextController {

    public static final String API_PATH = "api/public/texts";

    private final TextService textService;

    @Operation(summary = "Get all text overrides as a key-value map for a specific language")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Map<String, String>> getAllTexts(@RequestParam(defaultValue = "en") String lang) {
        Map<String, String> texts = textService.findByLanguage(lang).stream()
            .collect(Collectors.toMap(
                entity -> entity.getTranslationKey(),
                entity -> entity.getContentValue(),
                (existing, replacement) -> replacement
            ));
        return new ResponseEntity<>(texts, HttpStatus.OK);
    }
}
