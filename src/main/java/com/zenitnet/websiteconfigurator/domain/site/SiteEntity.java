package com.zenitnet.websiteconfigurator.domain.site;

import com.zenitnet.websiteconfigurator.common.domain.BaseEntity;
import com.zenitnet.websiteconfigurator.common.domain.DatabaseConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = DatabaseConstants.TableName.SITES)
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SiteEntity extends BaseEntity {

    @Column(name = DatabaseConstants.FieldName.Sites.NAME, nullable = false, length = 100)
    private String name;

    @Column(name = DatabaseConstants.FieldName.Sites.ROUTE_PATH, nullable = false, unique = true, length = 200)
    private String routePath;

    @Column(name = DatabaseConstants.FieldName.Sites.ACTIVE, nullable = false)
    private boolean active;

    /** How long each home page banner stays on screen, in seconds. */
    @Column(name = DatabaseConstants.FieldName.Sites.BANNER_INTERVAL_SECONDS, nullable = false)
    private int bannerIntervalSeconds;
}
