package com.sky.s1.hcm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ProbationDay.
 */
@Entity
@Table(name = "hcm_m_probation_day")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "probationday")
public class ProbationDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "company_code")
    private Long companyCode;

    @Column(name = "amount_date")
    private Integer amountDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public ProbationDay tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCompanyCode() {
        return companyCode;
    }

    public ProbationDay companyCode(Long companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public void setCompanyCode(Long companyCode) {
        this.companyCode = companyCode;
    }

    public Integer getAmountDate() {
        return amountDate;
    }

    public ProbationDay amountDate(Integer amountDate) {
        this.amountDate = amountDate;
        return this;
    }

    public void setAmountDate(Integer amountDate) {
        this.amountDate = amountDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProbationDay probationDay = (ProbationDay) o;
        if (probationDay.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), probationDay.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProbationDay{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", companyCode='" + getCompanyCode() + "'" +
            ", amountDate='" + getAmountDate() + "'" +
            "}";
    }
}
