package ua.sgkhmja.wboard.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Admin entity.
 */
public class AdminDTO implements Serializable {

    private Long id;

    private Long writerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWriterId() {
        return writerId;
    }

    public void setWriterId(Long writerId) {
        this.writerId = writerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AdminDTO adminDTO = (AdminDTO) o;
        if(adminDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), adminDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AdminDTO{" +
            "id=" + getId() +
            "}";
    }
}
