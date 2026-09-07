package com.zenitnet.websiteconfigurator.domain.banner;

import com.zenitnet.websiteconfigurator.domain.site.SiteEntity;
import com.zenitnet.websiteconfigurator.domain.site.SiteService;
import com.zenitnet.websiteconfigurator.domain.siteimage.SiteImageService;
import com.zenitnet.websiteconfigurator.domain.text.TextEntity;
import com.zenitnet.websiteconfigurator.domain.text.TextService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {

    /** Key prefix for banners created through the admin UI. */
    private static final String KEY_PREFIX = "home.banner.";
    /** Suffixes the front end reads under a banner key. */
    public static final String TITLE_SUFFIX = ".title";
    public static final String SUBTITLE_SUFFIX = ".subtitle";
    public static final String BACKGROUND_SUFFIX = ".background";

    private final BannerRepository bannerRepository;
    private final SiteService siteService;
    // The text and image repositories are package-private, so this goes through their
    // services -- which is the right layer anyway.
    private final TextService textService;
    private final SiteImageService siteImageService;

    public List<BannerEntity> findAllBySite(Long siteId) {
        return bannerRepository.findBySiteIdOrderByPositionAsc(siteId);
    }

    /** What the public page shows: visible banners only, in order. */
    public List<BannerEntity> findVisibleBySite(Long siteId) {
        return bannerRepository.findBySiteIdAndVisibleTrueOrderByPositionAsc(siteId);
    }

    public int getIntervalSeconds(Long siteId) {
        return siteService.findById(siteId).getBannerIntervalSeconds();
    }

    public void setIntervalSeconds(Long siteId, int seconds) {
        if (seconds < 2 || seconds > 120) {
            throw new IllegalArgumentException("A valtas ideje 2 es 120 masodperc kozott lehet");
        }
        SiteEntity site = siteService.findById(siteId);
        siteService.save(site.toBuilder().bannerIntervalSeconds(seconds).build());
    }

    /**
     * Creates an empty banner at the end of the list. Its text and image are then edited
     * through the existing text and image endpoints, under the returned key.
     */
    @Transactional
    public BannerEntity create(Long siteId) {
        SiteEntity site = siteService.findById(siteId);
        List<BannerEntity> existing = bannerRepository.findBySiteIdOrderByPositionAsc(siteId);

        int nextPosition = existing.isEmpty() ? 0 : existing.get(existing.size() - 1).getPosition() + 1;
        // The suffix is derived from the highest one in use rather than from the count,
        // so deleting a banner cannot make a new one collide with a key that still has
        // orphaned text rows behind it.
        long nextSuffix = existing.stream()
            .map(BannerEntity::getBannerKey)
            .filter(k -> k.startsWith(KEY_PREFIX))
            .map(k -> k.substring(KEY_PREFIX.length()))
            .filter(s -> s.chars().allMatch(Character::isDigit) && !s.isEmpty())
            .mapToLong(Long::parseLong)
            .max()
            .orElse(0L) + 1;

        return bannerRepository.save(BannerEntity.builder()
            .bannerKey(KEY_PREFIX + nextSuffix)
            .position(nextPosition)
            .visible(true)
            .site(site)
            .build());
    }

    @Transactional
    public BannerEntity setVisible(Long bannerId, boolean visible) {
        BannerEntity banner = findById(bannerId);
        return bannerRepository.save(banner.toBuilder().visible(visible).build());
    }

    /** Reorders a site's banners to the given id order; ids not listed keep their relative order after. */
    @Transactional
    public void reorder(Long siteId, List<Long> orderedIds) {
        List<BannerEntity> banners = bannerRepository.findBySiteIdOrderByPositionAsc(siteId);
        int position = 0;
        for (Long id : orderedIds) {
            BannerEntity match = banners.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Nincs ilyen banner ezen az oldalon: " + id));
            bannerRepository.save(match.toBuilder().position(position++).build());
        }
        for (BannerEntity b : banners) {
            if (!orderedIds.contains(b.getId())) {
                bannerRepository.save(b.toBuilder().position(position++).build());
            }
        }
    }

    /**
     * Deletes a banner together with the text and image rows that belong to its key.
     * Without that cleanup the rows would stay behind invisibly and a later banner could
     * inherit someone else's old content.
     */
    @Transactional
    public void delete(Long bannerId) {
        BannerEntity banner = findById(bannerId);
        Long siteId = banner.getSite().getId();

        if (bannerRepository.findBySiteIdOrderByPositionAsc(siteId).size() <= 1) {
            throw new IllegalArgumentException("Az utolso banner nem torolheto, kulonben ures lenne a kezdolap");
        }

        String key = banner.getBannerKey();
        for (TextEntity text : textService.findBySiteId(siteId)) {
            if (belongsToBanner(text.getTranslationKey(), key)) {
                textService.delete(text);
            }
        }
        siteImageService.deleteImage(key + BACKGROUND_SUFFIX, siteId);

        bannerRepository.delete(banner);
    }

    private boolean belongsToBanner(String translationKey, String bannerKey) {
        return translationKey.equals(bannerKey + TITLE_SUFFIX)
            || translationKey.equals(bannerKey + SUBTITLE_SUFFIX);
    }

    public BannerEntity findById(Long id) {
        return bannerRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Nincs ilyen banner: " + id));
    }
}
