package com.zenitnet.websiteconfigurator.domain.picture;

import com.zenitnet.websiteconfigurator.common.domain.BaseEntity;
import com.zenitnet.websiteconfigurator.common.domain.DatabaseConstants;
import com.zenitnet.websiteconfigurator.domain.site.SiteEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = DatabaseConstants.TableName.PICTURES)
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PictureEntity extends BaseEntity {

    @Column(name = DatabaseConstants.FieldName.Pictures.STORAGE_URL, nullable = false, length = 2048)
    private String storageUrl;

    @Column(name = DatabaseConstants.FieldName.Pictures.ALT_TEXT, nullable = false, length = 255)
    private String altText;

    @Column(name = DatabaseConstants.FieldName.Pictures.DISPLAY_ORDER, nullable = false)
    private int displayOrder;

    @ManyToOne
    @JoinColumn(name = DatabaseConstants.FieldName.Pictures.SITE_ID, nullable = false)
    private SiteEntity site;
}
