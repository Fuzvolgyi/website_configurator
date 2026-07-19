package com.zenitnet.websiteconfigurator.domain.site;

import com.zenitnet.websiteconfigurator.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.util.List;

@CommonsLog
@Service
@RequiredArgsConstructor
public class SiteService {

    private final SiteRepository siteRepository;

    public List<SiteEntity> findAll() {
        return siteRepository.findAllByOrderByNameAsc();
    }

    public SiteEntity findById(Long id) {
        return siteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Site not found with id: " + id));
    }

    public SiteEntity save(SiteEntity entity) {
        return siteRepository.save(entity);
    }
}
