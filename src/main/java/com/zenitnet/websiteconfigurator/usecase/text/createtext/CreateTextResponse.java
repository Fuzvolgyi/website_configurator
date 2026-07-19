package com.zenitnet.websiteconfigurator.usecase.text.createtext;

import lombok.Builder;

@Builder
public record CreateTextResponse(
    Long id,
    String translationKey,
    String contentValue,
    Long siteId
) {}
