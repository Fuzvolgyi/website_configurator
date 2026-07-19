package com.zenitnet.websiteconfigurator.domain.text;

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
@Table(name = DatabaseConstants.TableName.TEXTS, uniqueConstraints = @UniqueConstraint(
    columnNames = {DatabaseConstants.FieldName.Texts.TRANSLATION_KEY, DatabaseConstants.FieldName.Texts.SITE_ID, DatabaseConstants.FieldName.Texts.LANGUAGE}))
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TextEntity extends BaseEntity {

    @Column(name = DatabaseConstants.FieldName.Texts.TRANSLATION_KEY, nullable = false, length = 255)
    private String translationKey;

    @Column(name = DatabaseConstants.FieldName.Texts.CONTENT_VALUE, nullable = false, columnDefinition = "TEXT")
    private String contentValue;

    @Column(name = DatabaseConstants.FieldName.Texts.LANGUAGE, nullable = false, length = 10)
    private String language;

    @ManyToOne
    @JoinColumn(name = DatabaseConstants.FieldName.Texts.SITE_ID, nullable = false)
    private SiteEntity site;
}
