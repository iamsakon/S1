package com.sky.s1.hcm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ProbationPeriod.
 */
@Entity
@Table(name = "hcm_t_probation_period")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "probationperiod")
public class ProbationPeriod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "company_code")
    private Long companyCode;

    @Column(name = "period_start")
    private LocalDate periodStart;

    @Column(name = "period_end")
    private LocalDate periodEnd;

    @Column(name = "evaluate_period_start")
    private LocalDate evaluatePeriodStart;

    @Column(name = "evaluate_period_end")
    private LocalDate evaluatePeriodEnd;

    @ManyToOne
    private ProbationDay probation;

    @ManyToOne
    private Employee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public ProbationPeriod tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCompanyCode() {
        return companyCode;
    }

    public ProbationPeriod companyCode(Long companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public void setCompanyCode(Long companyCode) {
        this.companyCode = companyCode;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public ProbationPeriod periodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
        return this;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public ProbationPeriod periodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
        return this;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public LocalDate getEvaluatePeriodStart() {
        return evaluatePeriodStart;
    }

    public ProbationPeriod evaluatePeriodStart(LocalDate evaluatePeriodStart) {
        this.evaluatePeriodStart = evaluatePeriodStart;
        return this;
    }

    public void setEvaluatePeriodStart(LocalDate evaluatePeriodStart) {
        this.evaluatePeriodStart = evaluatePeriodStart;
    }

    public LocalDate getEvaluatePeriodEnd() {
        return evaluatePeriodEnd;
    }

    public ProbationPeriod evaluatePeriodEnd(LocalDate evaluatePeriodEnd) {
        this.evaluatePeriodEnd = evaluatePeriodEnd;
        return this;
    }

    public void setEvaluatePeriodEnd(LocalDate evaluatePeriodEnd) {
        this.evaluatePeriodEnd = evaluatePeriodEnd;
    }

    public ProbationDay getProbation() {
        return probation;
    }

    public ProbationPeriod probation(ProbationDay probationDay) {
        this.probation = probationDay;
        return this;
    }

    public void setProbation(ProbationDay probationDay) {
        this.probation = probationDay;
    }

    public Employee getEmployee() {
        return employee;
    }

    public ProbationPeriod employee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProbationPeriod probationPeriod = (ProbationPeriod) o;
        if (probationPeriod.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), probationPeriod.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProbationPeriod{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", companyCode='" + getCompanyCode() + "'" +
            ", periodStart='" + getPeriodStart() + "'" +
            ", periodEnd='" + getPeriodEnd() + "'" +
            ", evaluatePeriodStart='" + getEvaluatePeriodStart() + "'" +
            ", evaluatePeriodEnd='" + getEvaluatePeriodEnd() + "'" +
            "}";
    }
}
