package com.zenitnet.websiteconfigurator.controller.rest;

import com.zenitnet.websiteconfigurator.domain.banner.BannerEntity;
import com.zenitnet.websiteconfigurator.domain.banner.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "Banners", description = "Home page banner management")
@RequiredArgsConstructor
@RestController
@RequestMapping(BannerController.API_PATH)
public class BannerController {

    public static final String API_PATH = "api/admin/banners";

    private final BannerService bannerService;

    @Operation(summary = "List every banner of a site, in display order")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<Map<String, Object>>> list(@RequestParam("siteId") Long siteId) {
        return ResponseEntity.ok(bannerService.findAllBySite(siteId).stream().map(this::toDto).toList());
    }

    @Operation(summary = "Add an empty banner at the end; its text and image are edited under the returned key")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Map<String, Object>> create(@RequestParam("siteId") Long siteId) {
        return new ResponseEntity<>(toDto(bannerService.create(siteId)), HttpStatus.CREATED);
    }

    @Operation(summary = "Show or hide a banner")
    @PutMapping("/{id}/visible")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Map<String, Object>> setVisible(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        return ResponseEntity.ok(toDto(bannerService.setVisible(id, Boolean.TRUE.equals(body.get("visible")))));
    }

    @Operation(summary = "Set the display order for a site")
    @PutMapping("/order")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Void> reorder(@RequestParam("siteId") Long siteId, @RequestBody List<Long> orderedIds) {
        bannerService.reorder(siteId, orderedIds);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete a banner together with its text and image")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> delete(@PathVariable Long id) {
        bannerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "How long each banner stays on screen, in seconds")
    @GetMapping("/interval")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Map<String, Integer>> getInterval(@RequestParam("siteId") Long siteId) {
        return ResponseEntity.ok(Map.of("seconds", bannerService.getIntervalSeconds(siteId)));
    }

    @Operation(summary = "Change how long each banner stays on screen")
    @PutMapping("/interval")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Map<String, Integer>> setInterval(
        @RequestParam("siteId") Long siteId,
        @RequestBody Map<String, Integer> body
    ) {
        bannerService.setIntervalSeconds(siteId, body.getOrDefault("seconds", 8));
        return ResponseEntity.ok(Map.of("seconds", bannerService.getIntervalSeconds(siteId)));
    }

    private Map<String, Object> toDto(BannerEntity banner) {
        return Map.of(
            "id", banner.getId(),
            "bannerKey", banner.getBannerKey(),
            "position", banner.getPosition(),
            "visible", banner.isVisible()
        );
    }
}
