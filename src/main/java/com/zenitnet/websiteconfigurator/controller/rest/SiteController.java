package com.zenitnet.websiteconfigurator.controller.rest;

import com.zenitnet.websiteconfigurator.usecase.site.getallsites.GetAllSitesResponse;
import com.zenitnet.websiteconfigurator.usecase.site.getallsites.GetAllSitesUseCase;
import com.zenitnet.websiteconfigurator.usecase.site.updatesite.UpdateSiteRequest;
import com.zenitnet.websiteconfigurator.usecase.site.updatesite.UpdateSiteResponse;
import com.zenitnet.websiteconfigurator.usecase.site.updatesite.UpdateSiteUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Sites", description = "Site management endpoints")
@RequiredArgsConstructor
@RestController
@RequestMapping(SiteController.API_PATH)
public class SiteController {

    public static final String API_PATH = "api/admin/sites";

    private final GetAllSitesUseCase getAllSitesUseCase;
    private final UpdateSiteUseCase updateSiteUseCase;

    @Operation(summary = "Get all sites ordered by name")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<GetAllSitesResponse>> getAllSites() {
        return new ResponseEntity<>(getAllSitesUseCase.execute(), HttpStatus.OK);
    }

    @Operation(summary = "Update a site by ID")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<UpdateSiteResponse> updateSite(@PathVariable Long id, @RequestBody @Valid UpdateSiteRequest request) {
        return new ResponseEntity<>(updateSiteUseCase.execute(id, request), HttpStatus.OK);
    }
}
