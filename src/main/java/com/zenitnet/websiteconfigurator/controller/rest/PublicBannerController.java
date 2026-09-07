package com.zenitnet.websiteconfigurator.controller.rest;

import com.zenitnet.websiteconfigurator.domain.banner.BannerEntity;
import com.zenitnet.websiteconfigurator.domain.banner.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "Public Banners", description = "Public endpoint for the home page carousel")
@RequiredArgsConstructor
@RestController
@RequestMapping(PublicBannerController.API_PATH)
public class PublicBannerController {

    public static final String API_PATH = "api/public/banners";

    private final BannerService bannerService;

    /**
     * Returns only the visible banners, in order, plus how long each should stay on
     * screen. The page needs both in one call, and hidden banners must not reach the
     * browser at all -- an unfinished banner should not be one devtools panel away.
     */
    @Operation(summary = "Visible banners in display order, with the rotation interval")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Map<String, Object>> getVisibleBanners(@RequestParam("siteId") Long siteId) {
        List<Map<String, Object>> banners = bannerService.findVisibleBySite(siteId).stream()
            .map(this::toDto)
            .toList();
        return ResponseEntity.ok(Map.of(
            "intervalSeconds", bannerService.getIntervalSeconds(siteId),
            "banners", banners
        ));
    }

    private Map<String, Object> toDto(BannerEntity banner) {
        String key = banner.getBannerKey();
        return Map.of(
            "id", banner.getId(),
            "bannerKey", key,
            "titleKey", key + BannerService.TITLE_SUFFIX,
            "subtitleKey", key + BannerService.SUBTITLE_SUFFIX,
            "backgroundKey", key + BannerService.BACKGROUND_SUFFIX
        );
    }
}
