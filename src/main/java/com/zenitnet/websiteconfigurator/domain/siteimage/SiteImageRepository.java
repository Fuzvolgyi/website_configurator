package com.zenitnet.websiteconfigurator.domain.siteimage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface SiteImageRepository extends JpaRepository<SiteImageEntity, Long> {

    List<SiteImageEntity> findBySiteId(Long siteId);

    Optional<SiteImageEntity> findByImageKeyAndSiteId(String imageKey, Long siteId);
}
