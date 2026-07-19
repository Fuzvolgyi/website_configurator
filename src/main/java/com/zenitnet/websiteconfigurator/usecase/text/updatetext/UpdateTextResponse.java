package com.zenitnet.websiteconfigurator.usecase.text.updatetext;

import lombok.Builder;

@Builder
public record UpdateTextResponse(
    Long id,
    String translationKey,
    String contentValue,
    Long siteId
) {}
