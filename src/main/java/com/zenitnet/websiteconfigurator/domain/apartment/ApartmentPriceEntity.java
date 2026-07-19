package com.zenitnet.websiteconfigurator.domain.apartment;

import com.zenitnet.websiteconfigurator.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "apartment_prices")
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
class ApartmentPriceEntity extends BaseEntity {

    @Column(name = "apartment_slug", nullable = false, unique = true, length = 50)
    private String apartmentSlug;

    @Column(name = "apartment_name", nullable = false, length = 100)
    private String apartmentName;

    @Column(name = "price_per_night", nullable = false)
    private BigDecimal pricePerNight;
}
