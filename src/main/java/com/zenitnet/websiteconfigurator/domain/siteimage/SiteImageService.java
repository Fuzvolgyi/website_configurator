package com.zenitnet.websiteconfigurator.domain.siteimage;

import com.zenitnet.websiteconfigurator.domain.picture.StorageService;
import com.zenitnet.websiteconfigurator.domain.site.SiteEntity;
import com.zenitnet.websiteconfigurator.domain.site.SiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SiteImageService {

    private final SiteImageRepository siteImageRepository;
    private final SiteService siteService;
    private final StorageService storageService;

    public Map<String, String> getImagesBySiteId(Long siteId) {
        return siteImageRepository.findBySiteId(siteId).stream()
            .collect(Collectors.toMap(
                SiteImageEntity::getImageKey,
                SiteImageEntity::getStorageUrl,
                (existing, replacement) -> replacement
            ));
    }

    public Map<String, String> getAllImages() {
        return siteImageRepository.findAll().stream()
            .collect(Collectors.toMap(
                SiteImageEntity::getImageKey,
                SiteImageEntity::getStorageUrl,
                (existing, replacement) -> replacement
            ));
    }

    /**
     * Removes an image and the file behind it. Used when a banner is deleted: without it
     * the row and the object in storage would both stay behind, and a later banner
     * reusing the key would inherit the old picture.
     */
    public void deleteImage(String imageKey, Long siteId) {
        siteImageRepository.findByImageKeyAndSiteId(imageKey, siteId).ifPresent(image -> {
            storageService.deleteFile(image.getStorageUrl());
            siteImageRepository.delete(image);
        });
    }

    public void saveImage(String imageKey, String storageUrl, Long siteId) {
        SiteEntity site = siteService.findById(siteId);
        var existing = siteImageRepository.findByImageKeyAndSiteId(imageKey, siteId);

        if (existing.isPresent()) {
            storageService.deleteFile(existing.get().getStorageUrl());
            var updated = existing.get().toBuilder()
                .storageUrl(storageUrl)
                .build();
            siteImageRepository.save(updated);
        } else {
            var entity = SiteImageEntity.builder()
                .imageKey(imageKey)
                .storageUrl(storageUrl)
                .site(site)
                .build();
            siteImageRepository.save(entity);
        }
    }
}
