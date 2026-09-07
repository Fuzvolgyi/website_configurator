package com.zenitnet.websiteconfigurator.domain.banner;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface BannerRepository extends JpaRepository<BannerEntity, Long> {

    List<BannerEntity> findBySiteIdOrderByPositionAsc(Long siteId);

    List<BannerEntity> findBySiteIdAndVisibleTrueOrderByPositionAsc(Long siteId);

    Optional<BannerEntity> findByBannerKeyAndSiteId(String bannerKey, Long siteId);
}
