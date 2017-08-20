package com.sky.s1.hcm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A EmployeeContact.
 */
@Entity
@Table(name = "hcm_t_employee_contact")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "employeecontact")
public class EmployeeContact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "company_code")
    private Long companyCode;

    @Size(max = 256)
    @Column(name = "email", length = 256)
    private String email;

    @Size(max = 256)
    @Column(name = "mobile_number", length = 256)
    private String mobileNumber;

    @Size(max = 256)
    @Column(name = "phone_number", length = 256)
    private String phoneNumber;

    @Size(max = 128)
    @Column(name = "phone_no_extension", length = 128)
    private String phoneNoExtension;

    @Column(name = "active_date")
    private LocalDate activeDate;

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

    public EmployeeContact tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCompanyCode() {
        return companyCode;
    }

    public EmployeeContact companyCode(Long companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public void setCompanyCode(Long companyCode) {
        this.companyCode = companyCode;
    }

    public String getEmail() {
        return email;
    }

    public EmployeeContact email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public EmployeeContact mobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public EmployeeContact phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNoExtension() {
        return phoneNoExtension;
    }

    public EmployeeContact phoneNoExtension(String phoneNoExtension) {
        this.phoneNoExtension = phoneNoExtension;
        return this;
    }

    public void setPhoneNoExtension(String phoneNoExtension) {
        this.phoneNoExtension = phoneNoExtension;
    }

    public LocalDate getActiveDate() {
        return activeDate;
    }

    public EmployeeContact activeDate(LocalDate activeDate) {
        this.activeDate = activeDate;
        return this;
    }

    public void setActiveDate(LocalDate activeDate) {
        this.activeDate = activeDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public EmployeeContact employee(Employee employee) {
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
        EmployeeContact employeeContact = (EmployeeContact) o;
        if (employeeContact.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), employeeContact.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmployeeContact{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", companyCode='" + getCompanyCode() + "'" +
            ", email='" + getEmail() + "'" +
            ", mobileNumber='" + getMobileNumber() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", phoneNoExtension='" + getPhoneNoExtension() + "'" +
            ", activeDate='" + getActiveDate() + "'" +
            "}";
    }
}
