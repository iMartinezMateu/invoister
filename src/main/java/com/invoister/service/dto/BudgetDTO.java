package com.invoister.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Budget entity.
 */
public class BudgetDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    
    @Lob
    private String body;

    @NotNull
    private Double grossCost;

    @NotNull
    private Integer tax;

    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Double getGrossCost() {
        return grossCost;
    }

    public void setGrossCost(Double grossCost) {
        this.grossCost = grossCost;
    }

    public Integer getTax() {
        return tax;
    }

    public void setTax(Integer tax) {
        this.tax = tax;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BudgetDTO budgetDTO = (BudgetDTO) o;
        if (budgetDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), budgetDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BudgetDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", body='" + getBody() + "'" +
            ", grossCost=" + getGrossCost() +
            ", tax=" + getTax() +
            ", company=" + getCompanyId() +
            "}";
    }
}
