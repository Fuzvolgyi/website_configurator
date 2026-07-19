package com.zenitnet.websiteconfigurator.usecase.picture.updatepicture;

import com.zenitnet.websiteconfigurator.domain.picture.PictureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

@CommonsLog
@Component
@RequiredArgsConstructor
public class UpdatePictureUseCase {

    private final PictureService pictureService;

    public UpdatePictureResponse execute(Long id, UpdatePictureRequest request) {
        var entity = pictureService.findById(id);

        var updated = entity.toBuilder()
            .storageUrl(request.storageUrl())
            .altText(request.altText())
            .displayOrder(request.displayOrder())
            .build();

        var saved = pictureService.save(updated);

        return UpdatePictureResponse.builder()
            .id(saved.getId())
            .storageUrl(saved.getStorageUrl())
            .altText(saved.getAltText())
            .displayOrder(saved.getDisplayOrder())
            .siteId(saved.getSite().getId())
            .build();
    }
}
