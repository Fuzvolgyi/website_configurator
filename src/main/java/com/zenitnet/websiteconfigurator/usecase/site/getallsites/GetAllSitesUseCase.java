package com.zenitnet.websiteconfigurator.usecase.site.getallsites;

import com.zenitnet.websiteconfigurator.domain.site.SiteEntity;
import com.zenitnet.websiteconfigurator.domain.site.SiteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import java.util.List;

@CommonsLog
@Component
@RequiredArgsConstructor
public class GetAllSitesUseCase {

    private final SiteService siteService;

    public List<GetAllSitesResponse> execute() {
        return siteService.findAll().stream()
            .map(this::mapToResponse)
            .toList();
    }

    private GetAllSitesResponse mapToResponse(SiteEntity entity) {
        return GetAllSitesResponse.builder()
            .id(entity.getId())
            .name(entity.getName())
            .routePath(entity.getRoutePath())
            .active(entity.isActive())
            .build();
    }
}
