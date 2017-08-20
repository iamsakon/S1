package com.sky.s1.hcm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * Entity Prefix
 */
@ApiModel(description = "Entity Employee")
@Entity
@Table(name = "hcm_t_employee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "company_code")
    private Long companyCode;

    @NotNull
    @Size(max = 128)
    @Column(name = "employee_code", length = 128, nullable = false)
    private String employeeCode;

    @NotNull
    @Size(max = 256)
    @Column(name = "first_name", length = 256, nullable = false)
    private String firstName;

    @Size(max = 256)
    @Column(name = "middle_name", length = 256)
    private String middleName;

    @Size(max = 256)
    @Column(name = "last_name", length = 256)
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "joined_date")
    private LocalDate joinedDate;

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EmployeeContact> employeeContacts = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProbationPeriod> probationPeriods = new HashSet<>();

    @ManyToOne
    private Prefix prefix;

    @ManyToOne
    private Gender gender;

    @ManyToOne
    private BloodType bloodType;

    @ManyToOne
    private Nationality nationality;

    @ManyToOne
    private Race race;

    @ManyToOne
    private Religion religion;

    @ManyToOne
    private MilitaryStatus militaryStatus;

    @ManyToOne
    private MaritalStatus maritalStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public Employee tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCompanyCode() {
        return companyCode;
    }

    public Employee companyCode(Long companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public void setCompanyCode(Long companyCode) {
        this.companyCode = companyCode;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public Employee employeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
        return this;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public Employee firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public Employee middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public Employee lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Employee birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getJoinedDate() {
        return joinedDate;
    }

    public Employee joinedDate(LocalDate joinedDate) {
        this.joinedDate = joinedDate;
        return this;
    }

    public void setJoinedDate(LocalDate joinedDate) {
        this.joinedDate = joinedDate;
    }

    public Set<EmployeeContact> getEmployeeContacts() {
        return employeeContacts;
    }

    public Employee employeeContacts(Set<EmployeeContact> employeeContacts) {
        this.employeeContacts = employeeContacts;
        return this;
    }

    public Employee addEmployeeContact(EmployeeContact employeeContact) {
        this.employeeContacts.add(employeeContact);
        employeeContact.setEmployee(this);
        return this;
    }

    public Employee removeEmployeeContact(EmployeeContact employeeContact) {
        this.employeeContacts.remove(employeeContact);
        employeeContact.setEmployee(null);
        return this;
    }

    public void setEmployeeContacts(Set<EmployeeContact> employeeContacts) {
        this.employeeContacts = employeeContacts;
    }

    public Set<ProbationPeriod> getProbationPeriods() {
        return probationPeriods;
    }

    public Employee probationPeriods(Set<ProbationPeriod> probationPeriods) {
        this.probationPeriods = probationPeriods;
        return this;
    }

    public Employee addProbationPeriod(ProbationPeriod probationPeriod) {
        this.probationPeriods.add(probationPeriod);
        probationPeriod.setEmployee(this);
        return this;
    }

    public Employee removeProbationPeriod(ProbationPeriod probationPeriod) {
        this.probationPeriods.remove(probationPeriod);
        probationPeriod.setEmployee(null);
        return this;
    }

    public void setProbationPeriods(Set<ProbationPeriod> probationPeriods) {
        this.probationPeriods = probationPeriods;
    }

    public Prefix getPrefix() {
        return prefix;
    }

    public Employee prefix(Prefix prefix) {
        this.prefix = prefix;
        return this;
    }

    public void setPrefix(Prefix prefix) {
        this.prefix = prefix;
    }

    public Gender getGender() {
        return gender;
    }

    public Employee gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public Employee bloodType(BloodType bloodType) {
        this.bloodType = bloodType;
        return this;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public Employee nationality(Nationality nationality) {
        this.nationality = nationality;
        return this;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public Race getRace() {
        return race;
    }

    public Employee race(Race race) {
        this.race = race;
        return this;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Religion getReligion() {
        return religion;
    }

    public Employee religion(Religion religion) {
        this.religion = religion;
        return this;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public MilitaryStatus getMilitaryStatus() {
        return militaryStatus;
    }

    public Employee militaryStatus(MilitaryStatus militaryStatus) {
        this.militaryStatus = militaryStatus;
        return this;
    }

    public void setMilitaryStatus(MilitaryStatus militaryStatus) {
        this.militaryStatus = militaryStatus;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public Employee maritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
        return this;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        if (employee.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), employee.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", companyCode='" + getCompanyCode() + "'" +
            ", employeeCode='" + getEmployeeCode() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", middleName='" + getMiddleName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", joinedDate='" + getJoinedDate() + "'" +
            "}";
    }
}
