package com.invoister.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.invoister.domain.enumeration.SecureConnection;

/**
 * A DTO for the Notificator entity.
 */
public class NotificatorDTO implements Serializable {

    private Long id;

    @NotNull
    private String smtpHost;

    @NotNull
    private Integer smtpPort;

    @NotNull
    private Boolean smtpAuth;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private SecureConnection secureConnection;

    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public Integer getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(Integer smtpPort) {
        this.smtpPort = smtpPort;
    }

    public Boolean isSmtpAuth() {
        return smtpAuth;
    }

    public void setSmtpAuth(Boolean smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SecureConnection getSecureConnection() {
        return secureConnection;
    }

    public void setSecureConnection(SecureConnection secureConnection) {
        this.secureConnection = secureConnection;
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

        NotificatorDTO notificatorDTO = (NotificatorDTO) o;
        if (notificatorDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notificatorDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotificatorDTO{" +
            "id=" + getId() +
            ", smtpHost='" + getSmtpHost() + "'" +
            ", smtpPort=" + getSmtpPort() +
            ", smtpAuth='" + isSmtpAuth() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", secureConnection='" + getSecureConnection() + "'" +
            ", company=" + getCompanyId() +
            "}";
    }
}
