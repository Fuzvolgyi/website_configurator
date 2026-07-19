package com.zenitnet.websiteconfigurator.controller.rest;

import com.zenitnet.websiteconfigurator.usecase.picture.createpicture.CreatePictureRequest;
import com.zenitnet.websiteconfigurator.usecase.picture.createpicture.CreatePictureResponse;
import com.zenitnet.websiteconfigurator.usecase.picture.createpicture.CreatePictureUseCase;
import com.zenitnet.websiteconfigurator.usecase.picture.deletepicture.DeletePictureUseCase;
import com.zenitnet.websiteconfigurator.usecase.picture.getpictures.GetPicturesBySiteResponse;
import com.zenitnet.websiteconfigurator.usecase.picture.getpictures.GetPicturesBySiteUseCase;
import com.zenitnet.websiteconfigurator.usecase.picture.updatepicture.UpdatePictureRequest;
import com.zenitnet.websiteconfigurator.usecase.picture.updatepicture.UpdatePictureResponse;
import com.zenitnet.websiteconfigurator.usecase.picture.updatepicture.UpdatePictureUseCase;
import com.zenitnet.websiteconfigurator.usecase.picture.uploadpicture.UploadPictureResponse;
import com.zenitnet.websiteconfigurator.usecase.picture.uploadpicture.UploadPictureUseCase;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Pictures", description = "Picture management endpoints")
@RequiredArgsConstructor
@RestController
@RequestMapping(PictureController.API_PATH)
public class PictureController {

    public static final String API_PATH = "api/admin/pictures";

    private final GetPicturesBySiteUseCase getPicturesBySiteUseCase;
    private final CreatePictureUseCase createPictureUseCase;
    private final UpdatePictureUseCase updatePictureUseCase;
    private final DeletePictureUseCase deletePictureUseCase;
    private final UploadPictureUseCase uploadPictureUseCase;

    @Operation(summary = "Get pictures by site ID")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<GetPicturesBySiteResponse>> getPicturesBySite(@RequestParam Long siteId) {
        return new ResponseEntity<>(getPicturesBySiteUseCase.execute(siteId), HttpStatus.OK);
    }

    @Operation(summary = "Create a new picture")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<CreatePictureResponse> createPicture(@RequestBody @Valid CreatePictureRequest request) {
        return new ResponseEntity<>(createPictureUseCase.execute(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a picture by ID")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<UpdatePictureResponse> updatePicture(@PathVariable Long id, @RequestBody @Valid UpdatePictureRequest request) {
        return new ResponseEntity<>(updatePictureUseCase.execute(id, request), HttpStatus.OK);
    }

    @Operation(summary = "Delete a picture by ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> deletePicture(@PathVariable Long id) {
        deletePictureUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Upload a picture file to storage")
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<UploadPictureResponse> uploadPicture(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(uploadPictureUseCase.execute(file), HttpStatus.CREATED);
    }
}
