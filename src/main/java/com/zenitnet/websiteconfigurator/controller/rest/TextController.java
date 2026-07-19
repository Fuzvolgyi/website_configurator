package com.zenitnet.websiteconfigurator.controller.rest;

import com.zenitnet.websiteconfigurator.usecase.text.createtext.CreateTextRequest;
import com.zenitnet.websiteconfigurator.usecase.text.createtext.CreateTextResponse;
import com.zenitnet.websiteconfigurator.usecase.text.createtext.CreateTextUseCase;
import com.zenitnet.websiteconfigurator.usecase.text.deletetext.DeleteTextUseCase;
import com.zenitnet.websiteconfigurator.usecase.text.gettexts.GetTextsBySiteResponse;
import com.zenitnet.websiteconfigurator.usecase.text.gettexts.GetTextsBySiteUseCase;
import com.zenitnet.websiteconfigurator.usecase.text.savealltranslations.SaveAllTranslationsRequest;
import com.zenitnet.websiteconfigurator.usecase.text.savealltranslations.SaveAllTranslationsUseCase;
import com.zenitnet.websiteconfigurator.usecase.text.savewithtranslation.SaveTextWithTranslationRequest;
import com.zenitnet.websiteconfigurator.usecase.text.savewithtranslation.SaveTextWithTranslationResponse;
import com.zenitnet.websiteconfigurator.usecase.text.savewithtranslation.SaveTextWithTranslationUseCase;
import com.zenitnet.websiteconfigurator.usecase.text.updatetext.UpdateTextRequest;
import com.zenitnet.websiteconfigurator.usecase.text.updatetext.UpdateTextResponse;
import com.zenitnet.websiteconfigurator.usecase.text.updatetext.UpdateTextUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Texts", description = "Text content management endpoints")
@RequiredArgsConstructor
@RestController
@RequestMapping(TextController.API_PATH)
public class TextController {

    public static final String API_PATH = "api/admin/texts";

    private final GetTextsBySiteUseCase getTextsBySiteUseCase;
    private final CreateTextUseCase createTextUseCase;
    private final UpdateTextUseCase updateTextUseCase;
    private final DeleteTextUseCase deleteTextUseCase;
    private final SaveTextWithTranslationUseCase saveTextWithTranslationUseCase;
    private final SaveAllTranslationsUseCase saveAllTranslationsUseCase;

    @Operation(summary = "Get texts by site ID")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<GetTextsBySiteResponse>> getTextsBySite(@RequestParam Long siteId) {
        return new ResponseEntity<>(getTextsBySiteUseCase.execute(siteId), HttpStatus.OK);
    }

    @Operation(summary = "Create a new text entry")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<CreateTextResponse> createText(@RequestBody @Valid CreateTextRequest request) {
        return new ResponseEntity<>(createTextUseCase.execute(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a text entry by ID")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<UpdateTextResponse> updateText(@PathVariable Long id, @RequestBody @Valid UpdateTextRequest request) {
        return new ResponseEntity<>(updateTextUseCase.execute(id, request), HttpStatus.OK);
    }

    @Operation(summary = "Delete a text entry by ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> deleteText(@PathVariable Long id) {
        deleteTextUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Save text with auto-translation to all configured languages")
    @PostMapping("/translate")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<SaveTextWithTranslationResponse> saveWithTranslation(@RequestBody @Valid SaveTextWithTranslationRequest request) {
        return new ResponseEntity<>(saveTextWithTranslationUseCase.execute(request), HttpStatus.OK);
    }

    @Operation(summary = "Save all translations for a key at once (manual entry)")
    @PostMapping("/saveAll")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Void> saveAllTranslations(@RequestBody @Valid SaveAllTranslationsRequest request) {
        saveAllTranslationsUseCase.execute(request);
        return ResponseEntity.ok().build();
    }
}
