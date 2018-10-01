package com.invoister.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "c_id", nullable = false)
    private String cId;

    @NotNull
    @Column(name = "main_phone_number", nullable = false)
    private String mainPhoneNumber;

    @NotNull
    @Column(name = "secondary_phone_number", nullable = false)
    private String secondaryPhoneNumber;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "paypal_account")
    private String paypalAccount;

    @Column(name = "bank_account")
    private String bankAccount;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @Lob
    @Column(name = "secondary_logo")
    private byte[] secondaryLogo;

    @Column(name = "secondary_logo_content_type")
    private String secondaryLogoContentType;

    @Lob
    @Column(name = "stamp")
    private byte[] stamp;

    @Column(name = "stamp_content_type")
    private String stampContentType;

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Notificator> notificators = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Costumer> costumers = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Invoice> invoices = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Budget> budgets = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Report> reports = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Company title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getcId() {
        return cId;
    }

    public Company cId(String cId) {
        this.cId = cId;
        return this;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getMainPhoneNumber() {
        return mainPhoneNumber;
    }

    public Company mainPhoneNumber(String mainPhoneNumber) {
        this.mainPhoneNumber = mainPhoneNumber;
        return this;
    }

    public void setMainPhoneNumber(String mainPhoneNumber) {
        this.mainPhoneNumber = mainPhoneNumber;
    }

    public String getSecondaryPhoneNumber() {
        return secondaryPhoneNumber;
    }

    public Company secondaryPhoneNumber(String secondaryPhoneNumber) {
        this.secondaryPhoneNumber = secondaryPhoneNumber;
        return this;
    }

    public void setSecondaryPhoneNumber(String secondaryPhoneNumber) {
        this.secondaryPhoneNumber = secondaryPhoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public Company email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public Company address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaypalAccount() {
        return paypalAccount;
    }

    public Company paypalAccount(String paypalAccount) {
        this.paypalAccount = paypalAccount;
        return this;
    }

    public void setPaypalAccount(String paypalAccount) {
        this.paypalAccount = paypalAccount;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public Company bankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
        return this;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public byte[] getLogo() {
        return logo;
    }

    public Company logo(byte[] logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return logoContentType;
    }

    public Company logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public byte[] getSecondaryLogo() {
        return secondaryLogo;
    }

    public Company secondaryLogo(byte[] secondaryLogo) {
        this.secondaryLogo = secondaryLogo;
        return this;
    }

    public void setSecondaryLogo(byte[] secondaryLogo) {
        this.secondaryLogo = secondaryLogo;
    }

    public String getSecondaryLogoContentType() {
        return secondaryLogoContentType;
    }

    public Company secondaryLogoContentType(String secondaryLogoContentType) {
        this.secondaryLogoContentType = secondaryLogoContentType;
        return this;
    }

    public void setSecondaryLogoContentType(String secondaryLogoContentType) {
        this.secondaryLogoContentType = secondaryLogoContentType;
    }

    public byte[] getStamp() {
        return stamp;
    }

    public Company stamp(byte[] stamp) {
        this.stamp = stamp;
        return this;
    }

    public void setStamp(byte[] stamp) {
        this.stamp = stamp;
    }

    public String getStampContentType() {
        return stampContentType;
    }

    public Company stampContentType(String stampContentType) {
        this.stampContentType = stampContentType;
        return this;
    }

    public void setStampContentType(String stampContentType) {
        this.stampContentType = stampContentType;
    }

    public Set<Notificator> getNotificators() {
        return notificators;
    }

    public Company notificators(Set<Notificator> notificators) {
        this.notificators = notificators;
        return this;
    }

    public Company addNotificator(Notificator notificator) {
        this.notificators.add(notificator);
        notificator.setCompany(this);
        return this;
    }

    public Company removeNotificator(Notificator notificator) {
        this.notificators.remove(notificator);
        notificator.setCompany(null);
        return this;
    }

    public void setNotificators(Set<Notificator> notificators) {
        this.notificators = notificators;
    }

    public Set<Costumer> getCostumers() {
        return costumers;
    }

    public Company costumers(Set<Costumer> costumers) {
        this.costumers = costumers;
        return this;
    }

    public Company addCostumer(Costumer costumer) {
        this.costumers.add(costumer);
        costumer.setCompany(this);
        return this;
    }

    public Company removeCostumer(Costumer costumer) {
        this.costumers.remove(costumer);
        costumer.setCompany(null);
        return this;
    }

    public void setCostumers(Set<Costumer> costumers) {
        this.costumers = costumers;
    }

    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public Company invoices(Set<Invoice> invoices) {
        this.invoices = invoices;
        return this;
    }

    public Company addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setCompany(this);
        return this;
    }

    public Company removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.setCompany(null);
        return this;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }

    public Set<Budget> getBudgets() {
        return budgets;
    }

    public Company budgets(Set<Budget> budgets) {
        this.budgets = budgets;
        return this;
    }

    public Company addBudget(Budget budget) {
        this.budgets.add(budget);
        budget.setCompany(this);
        return this;
    }

    public Company removeBudget(Budget budget) {
        this.budgets.remove(budget);
        budget.setCompany(null);
        return this;
    }

    public void setBudgets(Set<Budget> budgets) {
        this.budgets = budgets;
    }

    public Set<Report> getReports() {
        return reports;
    }

    public Company reports(Set<Report> reports) {
        this.reports = reports;
        return this;
    }

    public Company addReport(Report report) {
        this.reports.add(report);
        report.setCompany(this);
        return this;
    }

    public Company removeReport(Report report) {
        this.reports.remove(report);
        report.setCompany(null);
        return this;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Company company = (Company) o;
        if (company.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), company.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Company{" +
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
            ", logoContentType='" + getLogoContentType() + "'" +
            ", secondaryLogo='" + getSecondaryLogo() + "'" +
            ", secondaryLogoContentType='" + getSecondaryLogoContentType() + "'" +
            ", stamp='" + getStamp() + "'" +
            ", stampContentType='" + getStampContentType() + "'" +
            "}";
    }
}
