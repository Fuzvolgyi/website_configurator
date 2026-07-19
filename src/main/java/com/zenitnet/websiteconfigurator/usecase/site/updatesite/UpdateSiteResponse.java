package com.zenitnet.websiteconfigurator.usecase.site.updatesite;

import lombok.Builder;

@Builder
public record UpdateSiteResponse(
    Long id,
    String name,
    String routePath,
    boolean active
) {}
