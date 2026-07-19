package com.zenitnet.websiteconfigurator.usecase.picture.deletepicture;

import com.zenitnet.websiteconfigurator.domain.picture.PictureService;
import com.zenitnet.websiteconfigurator.domain.picture.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

@CommonsLog
@Component
@RequiredArgsConstructor
public class DeletePictureUseCase {

    private final PictureService pictureService;
    private final StorageService storageService;

    public void execute(Long id) {
        var entity = pictureService.findById(id);
        storageService.deleteFile(entity.getStorageUrl());
        pictureService.delete(entity);
    }
}
