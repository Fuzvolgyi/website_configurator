package com.zenitnet.websiteconfigurator.controller.rest;

import com.zenitnet.websiteconfigurator.domain.apartment.ApartmentPriceService;
import com.zenitnet.websiteconfigurator.domain.apartment.ApartmentPriceService.ApartmentPriceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Tag(name = "Apartment Prices", description = "Apartment pricing management")
@RequiredArgsConstructor
@RestController
@RequestMapping(ApartmentPriceController.API_PATH)
public class ApartmentPriceController {

    public static final String API_PATH = "api/admin/apartmentPrices";

    private final ApartmentPriceService apartmentPriceService;

    @Operation(summary = "Get all apartment prices")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<ApartmentPriceDto>> getAll() {
        return new ResponseEntity<>(apartmentPriceService.getAll(), HttpStatus.OK);
    }

    @Operation(summary = "Update apartment price")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Void> updatePrice(@PathVariable Long id, @RequestBody Map<String, BigDecimal> body) {
        apartmentPriceService.updatePrice(id, body.get("pricePerNight"));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get prices as slug-to-price map (public)")
    @GetMapping("/public")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Map<String, BigDecimal>> getPublicPrices() {
        return new ResponseEntity<>(apartmentPriceService.getAllAsMap(), HttpStatus.OK);
    }
}
