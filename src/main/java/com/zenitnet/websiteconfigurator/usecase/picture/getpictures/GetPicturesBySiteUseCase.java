package com.zenitnet.websiteconfigurator.usecase.picture.getpictures;

import com.zenitnet.websiteconfigurator.domain.picture.PictureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import java.util.List;

@CommonsLog
@Component
@RequiredArgsConstructor
public class GetPicturesBySiteUseCase {

    private final PictureService pictureService;

    public List<GetPicturesBySiteResponse> execute(Long siteId) {
        return pictureService.findBySiteId(siteId).stream()
            .map(entity -> GetPicturesBySiteResponse.builder()
                .id(entity.getId())
                .storageUrl(entity.getStorageUrl())
                .altText(entity.getAltText())
                .displayOrder(entity.getDisplayOrder())
                .siteId(entity.getSite().getId())
                .build())
            .toList();
    }
}
