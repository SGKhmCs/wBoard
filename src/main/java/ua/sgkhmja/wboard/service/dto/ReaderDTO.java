package ua.sgkhmja.wboard.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Reader entity.
 */
public class ReaderDTO implements Serializable {

    private Long id;

    private Long boardUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBoardUserId() {
        return boardUserId;
    }

    public void setBoardUserId(Long boardUserId) {
        this.boardUserId = boardUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReaderDTO readerDTO = (ReaderDTO) o;
        if(readerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), readerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReaderDTO{" +
            "id=" + getId() +
            "}";
    }
}
