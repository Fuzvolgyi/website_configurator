package com.zenitnet.websiteconfigurator.usecase.picture.updatepicture;

import lombok.Builder;

@Builder
public record UpdatePictureResponse(
    Long id,
    String storageUrl,
    String altText,
    int displayOrder,
    Long siteId
) {}
