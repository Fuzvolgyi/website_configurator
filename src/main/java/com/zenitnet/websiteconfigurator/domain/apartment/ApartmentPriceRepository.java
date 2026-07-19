package com.zenitnet.websiteconfigurator.domain.apartment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface ApartmentPriceRepository extends JpaRepository<ApartmentPriceEntity, Long> {

    Optional<ApartmentPriceEntity> findByApartmentSlug(String apartmentSlug);
}
