package com.zenitnet.websiteconfigurator.usecase.picture.uploadpicture;

import com.zenitnet.websiteconfigurator.common.exception.InvalidParameterException;
import com.zenitnet.websiteconfigurator.domain.picture.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@CommonsLog
@Component
@RequiredArgsConstructor
public class UploadPictureUseCase {

    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024;
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
        "image/jpeg",
        "image/png",
        "image/webp"
    );

    private final StorageService storageService;

    public UploadPictureResponse execute(MultipartFile file) {
        validateFile(file);
        String url = storageService.uploadFile(file);
        return UploadPictureResponse.builder().url(url).build();
    }

    private void validateFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new InvalidParameterException("Invalid file format. Accepted formats: JPEG, PNG, WebP");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidParameterException("File size exceeds the maximum allowed size of 5 MB");
        }
    }
}
