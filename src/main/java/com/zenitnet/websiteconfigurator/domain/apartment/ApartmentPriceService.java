package com.zenitnet.websiteconfigurator.domain.apartment;

import com.zenitnet.websiteconfigurator.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApartmentPriceService {

    private final ApartmentPriceRepository repository;

    public List<ApartmentPriceDto> getAll() {
        return repository.findAll().stream()
            .map(e -> new ApartmentPriceDto(e.getId(), e.getApartmentSlug(), e.getApartmentName(), e.getPricePerNight()))
            .toList();
    }

    public Map<String, BigDecimal> getAllAsMap() {
        return repository.findAll().stream()
            .collect(Collectors.toMap(
                ApartmentPriceEntity::getApartmentSlug,
                ApartmentPriceEntity::getPricePerNight
            ));
    }

    public void updatePrice(Long id, BigDecimal newPrice) {
        var entity = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Apartment price not found with id: " + id));
        var updated = entity.toBuilder()
            .pricePerNight(newPrice)
            .build();
        repository.save(updated);
    }

    public record ApartmentPriceDto(Long id, String apartmentSlug, String apartmentName, BigDecimal pricePerNight) {}
}
