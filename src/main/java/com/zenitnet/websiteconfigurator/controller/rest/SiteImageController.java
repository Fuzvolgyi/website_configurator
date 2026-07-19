package com.zenitnet.websiteconfigurator.controller.rest;

import com.zenitnet.websiteconfigurator.domain.picture.StorageService;
import com.zenitnet.websiteconfigurator.domain.siteimage.SiteImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Tag(name = "Site Images", description = "Site image management endpoints")
@RequiredArgsConstructor
@RestController
@RequestMapping(SiteImageController.API_PATH)
public class SiteImageController {

    public static final String API_PATH = "api/admin/siteImages";

    private final SiteImageService siteImageService;
    private final StorageService storageService;

    @Operation(summary = "Upload and stage a site image (not yet committed)")
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Map<String, String>> uploadImage(
        @RequestParam("file") MultipartFile file,
        @RequestParam("imageKey") String imageKey,
        @RequestParam("siteId") Long siteId
    ) {
        String url = storageService.uploadFile(file);
        return new ResponseEntity<>(Map.of("url", url, "imageKey", imageKey), HttpStatus.OK);
    }

    @Operation(summary = "Confirm a staged image — saves to DB, deletes old from S3")
    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Void> confirmImage(@RequestBody Map<String, String> body) {
        String imageKey = body.get("imageKey");
        String newUrl = body.get("newUrl");
        Long siteId = Long.valueOf(body.get("siteId"));
        siteImageService.saveImage(imageKey, newUrl, siteId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Revert a staged image — deletes the new upload from S3")
    @PostMapping("/revert")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Void> revertImage(@RequestBody Map<String, String> body) {
        String url = body.get("url");
        storageService.deleteFile(url);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all site images as key-value map")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Map<String, String>> getAllImages() {
        return new ResponseEntity<>(siteImageService.getAllImages(), HttpStatus.OK);
    }
}
