package com.zenitnet.websiteconfigurator.domain.banner;

import com.zenitnet.websiteconfigurator.common.domain.BaseEntity;
import com.zenitnet.websiteconfigurator.common.domain.DatabaseConstants;
import com.zenitnet.websiteconfigurator.domain.site.SiteEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * One slide of the home page carousel.
 *
 * <p>A banner deliberately stores no text and no image. It holds a key prefix, and the
 * content lives where all other content lives: {@code <bannerKey>.title} and
 * {@code <bannerKey>.subtitle} in the texts table, {@code <bannerKey>.background} in
 * site_images. Translation and image upload therefore keep working unchanged, and a
 * banner is edited with the same tooling as the rest of the page.
 */
@Entity
@Table(name = DatabaseConstants.TableName.BANNERS)
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BannerEntity extends BaseEntity {

    @Column(name = DatabaseConstants.FieldName.Banners.BANNER_KEY, nullable = false, length = 255)
    private String bannerKey;

    @Column(name = DatabaseConstants.FieldName.Banners.POSITION, nullable = false)
    private int position;

    @Column(name = DatabaseConstants.FieldName.Banners.VISIBLE, nullable = false)
    private boolean visible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = DatabaseConstants.FieldName.Banners.SITE_ID, nullable = false)
    private SiteEntity site;
}
