package com.invoister.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Budget.
 */
@Entity
@Table(name = "budget")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "budget")
public class Budget implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private LocalDate date;

    
    @Lob
    @Column(name = "jhi_body", nullable = false)
    private String body;

    @NotNull
    @Column(name = "gross_cost", nullable = false)
    private Double grossCost;

    @NotNull
    @Column(name = "tax", nullable = false)
    private Integer tax;

    @ManyToOne
    @JsonIgnoreProperties("budgets")
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Budget date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public Budget body(String body) {
        this.body = body;
        return this;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Double getGrossCost() {
        return grossCost;
    }

    public Budget grossCost(Double grossCost) {
        this.grossCost = grossCost;
        return this;
    }

    public void setGrossCost(Double grossCost) {
        this.grossCost = grossCost;
    }

    public Integer getTax() {
        return tax;
    }

    public Budget tax(Integer tax) {
        this.tax = tax;
        return this;
    }

    public void setTax(Integer tax) {
        this.tax = tax;
    }

    public Company getCompany() {
        return company;
    }

    public Budget company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
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
        Budget budget = (Budget) o;
        if (budget.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), budget.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Budget{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", body='" + getBody() + "'" +
            ", grossCost=" + getGrossCost() +
            ", tax=" + getTax() +
            "}";
    }
}
