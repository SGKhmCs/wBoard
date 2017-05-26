package ua.sgkhmja.wboard.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the AdminTools entity.
 */
public class AdminToolsDTO implements Serializable {

    private Long id;

    private Long adminId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AdminToolsDTO adminToolsDTO = (AdminToolsDTO) o;
        if(adminToolsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), adminToolsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AdminToolsDTO{" +
            "id=" + getId() +
            "}";
    }
}
