package com.zenitnet.websiteconfigurator.usecase.picture.createpicture;

import com.zenitnet.websiteconfigurator.domain.picture.PictureEntity;
import com.zenitnet.websiteconfigurator.domain.picture.PictureService;
import com.zenitnet.websiteconfigurator.domain.site.SiteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

@CommonsLog
@Component
@RequiredArgsConstructor
public class CreatePictureUseCase {

    private final PictureService pictureService;
    private final SiteService siteService;

    public CreatePictureResponse execute(CreatePictureRequest request) {
        var site = siteService.findById(request.siteId());

        var entity = PictureEntity.builder()
            .storageUrl(request.storageUrl())
            .altText(request.altText())
            .displayOrder(request.displayOrder())
            .site(site)
            .build();

        var saved = pictureService.save(entity);

        return CreatePictureResponse.builder()
            .id(saved.getId())
            .storageUrl(saved.getStorageUrl())
            .altText(saved.getAltText())
            .displayOrder(saved.getDisplayOrder())
            .siteId(saved.getSite().getId())
            .build();
    }
}
