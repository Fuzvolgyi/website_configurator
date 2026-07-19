package com.zenitnet.websiteconfigurator.usecase.text.deletetext;

import com.zenitnet.websiteconfigurator.domain.text.TextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

@CommonsLog
@Component
@RequiredArgsConstructor
public class DeleteTextUseCase {

    private final TextService textService;

    public void execute(Long id) {
        var entity = textService.findById(id);
        textService.delete(entity);
    }
}
