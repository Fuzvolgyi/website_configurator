package com.zenitnet.websiteconfigurator.usecase.picture.uploadpicture;

import lombok.Builder;

@Builder
public record UploadPictureResponse(
    String url
) {}
