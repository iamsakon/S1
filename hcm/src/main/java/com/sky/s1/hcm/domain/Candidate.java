package com.sky.s1.hcm.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Entity Prefix
 */
@ApiModel(description = "Entity Prefix")
@Entity
@Table(name = "candidate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "candidate")
public class Candidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 128)
    @Column(name = "code", length = 128, nullable = false)
    private String code;

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

    @Size(max = 256)
    @Column(name = "personal_email", length = 256)
    private String personalEmail;

    @Size(max = 256)
    @Column(name = "mobile_number", length = 256)
    private String mobileNumber;

    @Size(max = 256)
    @Column(name = "phone_number", length = 256)
    private String phoneNumber;

    @NotNull
    @Column(name = "active_flag", nullable = false)
    private Boolean activeFlag;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "company_code")
    private Long companyCode;

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

    public String getCode() {
        return code;
    }

    public Candidate code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFirstName() {
        return firstName;
    }

    public Candidate firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public Candidate middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public Candidate lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Candidate birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public Candidate personalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
        return this;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public Candidate mobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Candidate phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean isActiveFlag() {
        return activeFlag;
    }

    public Candidate activeFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
        return this;
    }

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public Candidate tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCompanyCode() {
        return companyCode;
    }

    public Candidate companyCode(Long companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public void setCompanyCode(Long companyCode) {
        this.companyCode = companyCode;
    }

    public Prefix getPrefix() {
        return prefix;
    }

    public Candidate prefix(Prefix prefix) {
        this.prefix = prefix;
        return this;
    }

    public void setPrefix(Prefix prefix) {
        this.prefix = prefix;
    }

    public Gender getGender() {
        return gender;
    }

    public Candidate gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public Candidate bloodType(BloodType bloodType) {
        this.bloodType = bloodType;
        return this;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public Candidate nationality(Nationality nationality) {
        this.nationality = nationality;
        return this;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public Race getRace() {
        return race;
    }

    public Candidate race(Race race) {
        this.race = race;
        return this;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Religion getReligion() {
        return religion;
    }

    public Candidate religion(Religion religion) {
        this.religion = religion;
        return this;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public MilitaryStatus getMilitaryStatus() {
        return militaryStatus;
    }

    public Candidate militaryStatus(MilitaryStatus militaryStatus) {
        this.militaryStatus = militaryStatus;
        return this;
    }

    public void setMilitaryStatus(MilitaryStatus militaryStatus) {
        this.militaryStatus = militaryStatus;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public Candidate maritalStatus(MaritalStatus maritalStatus) {
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
        Candidate candidate = (Candidate) o;
        if (candidate.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), candidate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Candidate{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", middleName='" + getMiddleName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", personalEmail='" + getPersonalEmail() + "'" +
            ", mobileNumber='" + getMobileNumber() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", activeFlag='" + isActiveFlag() + "'" +
            ", tenantId='" + getTenantId() + "'" +
            ", companyCode='" + getCompanyCode() + "'" +
            "}";
    }
}
