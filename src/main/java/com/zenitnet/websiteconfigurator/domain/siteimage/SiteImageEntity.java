package com.zenitnet.websiteconfigurator.domain.siteimage;

import com.zenitnet.websiteconfigurator.common.domain.BaseEntity;
import com.zenitnet.websiteconfigurator.common.domain.DatabaseConstants;
import com.zenitnet.websiteconfigurator.domain.site.SiteEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = DatabaseConstants.TableName.SITE_IMAGES, uniqueConstraints = @UniqueConstraint(
    columnNames = {DatabaseConstants.FieldName.SiteImages.IMAGE_KEY, DatabaseConstants.FieldName.SiteImages.SITE_ID}))
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
class SiteImageEntity extends BaseEntity {

    @Column(name = DatabaseConstants.FieldName.SiteImages.IMAGE_KEY, nullable = false, length = 255)
    private String imageKey;

    @Column(name = DatabaseConstants.FieldName.SiteImages.STORAGE_URL, nullable = false, columnDefinition = "TEXT")
    private String storageUrl;

    @ManyToOne
    @JoinColumn(name = DatabaseConstants.FieldName.SiteImages.SITE_ID, nullable = false)
    private SiteEntity site;
}
