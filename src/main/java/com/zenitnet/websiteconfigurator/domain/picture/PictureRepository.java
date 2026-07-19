package com.zenitnet.websiteconfigurator.domain.picture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface PictureRepository extends JpaRepository<PictureEntity, Long> {

    List<PictureEntity> findBySiteIdOrderByDisplayOrderAsc(Long siteId);
}
