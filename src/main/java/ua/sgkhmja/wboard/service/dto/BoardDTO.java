package ua.sgkhmja.wboard.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Board entity.
 */
public class BoardDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 48)
    private String name;

    private Boolean pub;

    private Long ownerId;

    private String ownerLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isPub() {
        return pub;
    }

    public void setPub(Boolean pub) {
        this.pub = pub;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long userId) {
        this.ownerId = userId;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String userLogin) {
        this.ownerLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BoardDTO boardDTO = (BoardDTO) o;
        if(boardDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), boardDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BoardDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", pub='" + isPub() + "'" +
            "}";
    }
}
