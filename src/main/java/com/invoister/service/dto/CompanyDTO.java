package com.invoister.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Company entity.
 */
public class CompanyDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String cId;

    @NotNull
    private String mainPhoneNumber;

    @NotNull
    private String secondaryPhoneNumber;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    private String email;

    @NotNull
    private String address;

    private String paypalAccount;

    private String bankAccount;

    @Lob
    private byte[] logo;
    private String logoContentType;

    @Lob
    private byte[] secondaryLogo;
    private String secondaryLogoContentType;

    @Lob
    private byte[] stamp;
    private String stampContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getMainPhoneNumber() {
        return mainPhoneNumber;
    }

    public void setMainPhoneNumber(String mainPhoneNumber) {
        this.mainPhoneNumber = mainPhoneNumber;
    }

    public String getSecondaryPhoneNumber() {
        return secondaryPhoneNumber;
    }

    public void setSecondaryPhoneNumber(String secondaryPhoneNumber) {
        this.secondaryPhoneNumber = secondaryPhoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaypalAccount() {
        return paypalAccount;
    }

    public void setPaypalAccount(String paypalAccount) {
        this.paypalAccount = paypalAccount;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return logoContentType;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public byte[] getSecondaryLogo() {
        return secondaryLogo;
    }

    public void setSecondaryLogo(byte[] secondaryLogo) {
        this.secondaryLogo = secondaryLogo;
    }

    public String getSecondaryLogoContentType() {
        return secondaryLogoContentType;
    }

    public void setSecondaryLogoContentType(String secondaryLogoContentType) {
        this.secondaryLogoContentType = secondaryLogoContentType;
    }

    public byte[] getStamp() {
        return stamp;
    }

    public void setStamp(byte[] stamp) {
        this.stamp = stamp;
    }

    public String getStampContentType() {
        return stampContentType;
    }

    public void setStampContentType(String stampContentType) {
        this.stampContentType = stampContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CompanyDTO companyDTO = (CompanyDTO) o;
        if (companyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), companyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CompanyDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", cId='" + getcId() + "'" +
            ", mainPhoneNumber='" + getMainPhoneNumber() + "'" +
            ", secondaryPhoneNumber='" + getSecondaryPhoneNumber() + "'" +
            ", email='" + getEmail() + "'" +
            ", address='" + getAddress() + "'" +
            ", paypalAccount='" + getPaypalAccount() + "'" +
            ", bankAccount='" + getBankAccount() + "'" +
            ", logo='" + getLogo() + "'" +
            ", secondaryLogo='" + getSecondaryLogo() + "'" +
            ", stamp='" + getStamp() + "'" +
            "}";
    }
}
