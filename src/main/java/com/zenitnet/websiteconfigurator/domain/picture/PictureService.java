package com.zenitnet.websiteconfigurator.domain.picture;

import com.zenitnet.websiteconfigurator.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.util.List;

@CommonsLog
@Service
@RequiredArgsConstructor
public class PictureService {

    private final PictureRepository pictureRepository;

    public List<PictureEntity> findBySiteId(Long siteId) {
        return pictureRepository.findBySiteIdOrderByDisplayOrderAsc(siteId);
    }

    public PictureEntity findById(Long id) {
        return pictureRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Picture not found with id: " + id));
    }

    public PictureEntity save(PictureEntity entity) {
        return pictureRepository.save(entity);
    }

    public void delete(PictureEntity entity) {
        pictureRepository.delete(entity);
    }
}
