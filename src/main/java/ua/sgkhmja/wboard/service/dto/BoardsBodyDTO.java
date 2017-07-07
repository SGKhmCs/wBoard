package ua.sgkhmja.wboard.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the BoardsBody entity.
 */
public class BoardsBodyDTO implements Serializable {

    private Long id;

    private Integer backgroundColor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Integer backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BoardsBodyDTO boardsBodyDTO = (BoardsBodyDTO) o;
        if(boardsBodyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), boardsBodyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BoardsBodyDTO{" +
            "id=" + getId() +
            ", backgroundColor='" + getBackgroundColor() + "'" +
            "}";
    }
}
