package com.zenitnet.websiteconfigurator.usecase.site.updatesite;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateSiteRequest(
    @NotBlank @Size(max = 100) String name,
    @NotBlank @Size(max = 200) @Pattern(regexp = "^/[a-z0-9-]+$") String routePath,
    boolean active
) {}
