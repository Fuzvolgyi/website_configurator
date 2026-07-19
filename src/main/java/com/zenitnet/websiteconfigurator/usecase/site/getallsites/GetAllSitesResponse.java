package com.zenitnet.websiteconfigurator.usecase.site.getallsites;

import lombok.Builder;

@Builder
public record GetAllSitesResponse(
    Long id,
    String name,
    String routePath,
    boolean active
) {}
