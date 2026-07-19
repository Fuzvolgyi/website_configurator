package com.zenitnet.websiteconfigurator.usecase.picture.getpictures;

import lombok.Builder;

@Builder
public record GetPicturesBySiteResponse(
    Long id,
    String storageUrl,
    String altText,
    int displayOrder,
    Long siteId
) {}
