package com.sky.s1.hcm.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Entity Prefix
 */
@ApiModel(description = "Entity Prefix")
@Entity
@Table(name = "hcm_m_prefix")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "prefix")
public class Prefix implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 128)
    @Column(name = "code", length = 128, nullable = false)
    private String code;

    @Size(max = 256)
    @Column(name = "name", length = 256)
    private String name;

    @Size(max = 1024)
    @Column(name = "description", length = 1024)
    private String description;

    @NotNull
    @Column(name = "active_flag", nullable = false)
    private Boolean activeFlag;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "company_code")
    private Long companyCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Prefix code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public Prefix name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Prefix description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isActiveFlag() {
        return activeFlag;
    }

    public Prefix activeFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
        return this;
    }

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public Prefix tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCompanyCode() {
        return companyCode;
    }

    public Prefix companyCode(Long companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public void setCompanyCode(Long companyCode) {
        this.companyCode = companyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Prefix prefix = (Prefix) o;
        if (prefix.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), prefix.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Prefix{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", activeFlag='" + isActiveFlag() + "'" +
            ", tenantId='" + getTenantId() + "'" +
            ", companyCode='" + getCompanyCode() + "'" +
            "}";
    }
}
