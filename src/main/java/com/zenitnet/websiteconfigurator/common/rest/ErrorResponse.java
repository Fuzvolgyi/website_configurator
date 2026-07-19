package com.zenitnet.websiteconfigurator.common.rest;

import lombok.Builder;

import java.util.List;

@Builder
public record ErrorResponse(
    String message,
    List<FieldError> fieldErrors
) {

    @Builder
    public record FieldError(
        String field,
        String message
    ) {}
}
