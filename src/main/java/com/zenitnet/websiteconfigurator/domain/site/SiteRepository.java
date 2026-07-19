package com.zenitnet.websiteconfigurator.domain.site;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SiteRepository extends JpaRepository<SiteEntity, Long> {

    List<SiteEntity> findAllByOrderByNameAsc();
}
