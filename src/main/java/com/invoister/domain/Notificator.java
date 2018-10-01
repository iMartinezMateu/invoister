package com.invoister.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import com.invoister.domain.enumeration.SecureConnection;

/**
 * A Notificator.
 */
@Entity
@Table(name = "notificator")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "notificator")
public class Notificator implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "smtp_host", nullable = false)
    private String smtpHost;

    @NotNull
    @Column(name = "smtp_port", nullable = false)
    private Integer smtpPort;

    @NotNull
    @Column(name = "smtp_auth", nullable = false)
    private Boolean smtpAuth;

    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

    @NotNull
    @Column(name = "jhi_password", nullable = false)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "secure_connection", nullable = false)
    private SecureConnection secureConnection;

    @ManyToOne
    @JsonIgnoreProperties("notificators")
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public Notificator smtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
        return this;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public Integer getSmtpPort() {
        return smtpPort;
    }

    public Notificator smtpPort(Integer smtpPort) {
        this.smtpPort = smtpPort;
        return this;
    }

    public void setSmtpPort(Integer smtpPort) {
        this.smtpPort = smtpPort;
    }

    public Boolean isSmtpAuth() {
        return smtpAuth;
    }

    public Notificator smtpAuth(Boolean smtpAuth) {
        this.smtpAuth = smtpAuth;
        return this;
    }

    public void setSmtpAuth(Boolean smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    public String getUsername() {
        return username;
    }

    public Notificator username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public Notificator password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SecureConnection getSecureConnection() {
        return secureConnection;
    }

    public Notificator secureConnection(SecureConnection secureConnection) {
        this.secureConnection = secureConnection;
        return this;
    }

    public void setSecureConnection(SecureConnection secureConnection) {
        this.secureConnection = secureConnection;
    }

    public Company getCompany() {
        return company;
    }

    public Notificator company(Company company) {
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
        Notificator notificator = (Notificator) o;
        if (notificator.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notificator.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Notificator{" +
            "id=" + getId() +
            ", smtpHost='" + getSmtpHost() + "'" +
            ", smtpPort=" + getSmtpPort() +
            ", smtpAuth='" + isSmtpAuth() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", secureConnection='" + getSecureConnection() + "'" +
            "}";
    }
}
