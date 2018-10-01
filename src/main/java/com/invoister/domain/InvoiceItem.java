package com.invoister.domain;

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
 * A InvoiceItem.
 */
@Entity
@Table(name = "invoice_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "invoiceitem")
public class InvoiceItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "gross_cost", nullable = false)
    private Double grossCost;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "invoice_item_invoice",
               joinColumns = @JoinColumn(name = "invoice_items_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "invoices_id", referencedColumnName = "id"))
    private Set<Invoice> invoices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public InvoiceItem description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public InvoiceItem quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getGrossCost() {
        return grossCost;
    }

    public InvoiceItem grossCost(Double grossCost) {
        this.grossCost = grossCost;
        return this;
    }

    public void setGrossCost(Double grossCost) {
        this.grossCost = grossCost;
    }

    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public InvoiceItem invoices(Set<Invoice> invoices) {
        this.invoices = invoices;
        return this;
    }

    public InvoiceItem addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.getInvoiceItems().add(this);
        return this;
    }

    public InvoiceItem removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.getInvoiceItems().remove(this);
        return this;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
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
        InvoiceItem invoiceItem = (InvoiceItem) o;
        if (invoiceItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoiceItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvoiceItem{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", quantity=" + getQuantity() +
            ", grossCost=" + getGrossCost() +
            "}";
    }
}
