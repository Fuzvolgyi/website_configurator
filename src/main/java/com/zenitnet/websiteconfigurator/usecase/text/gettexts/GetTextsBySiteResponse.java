package com.zenitnet.websiteconfigurator.usecase.text.gettexts;

import lombok.Builder;

@Builder
public record GetTextsBySiteResponse(
    Long id,
    String translationKey,
    String contentValue,
    Long siteId
) {}
