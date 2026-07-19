package com.zenitnet.websiteconfigurator.usecase.site.updatesite;

import com.zenitnet.websiteconfigurator.domain.site.SiteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

@CommonsLog
@Component
@RequiredArgsConstructor
public class UpdateSiteUseCase {

    private final SiteService siteService;

    public UpdateSiteResponse execute(Long id, UpdateSiteRequest request) {
        var entity = siteService.findById(id);

        var updated = entity.toBuilder()
            .name(request.name())
            .routePath(request.routePath())
            .active(request.active())
            .build();

        var saved = siteService.save(updated);

        return UpdateSiteResponse.builder()
            .id(saved.getId())
            .name(saved.getName())
            .routePath(saved.getRoutePath())
            .active(saved.isActive())
            .build();
    }
}
