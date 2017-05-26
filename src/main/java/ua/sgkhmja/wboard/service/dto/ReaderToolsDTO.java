package ua.sgkhmja.wboard.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ReaderTools entity.
 */
public class ReaderToolsDTO implements Serializable {

    private Long id;

    private Long readerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReaderId() {
        return readerId;
    }

    public void setReaderId(Long readerId) {
        this.readerId = readerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReaderToolsDTO readerToolsDTO = (ReaderToolsDTO) o;
        if(readerToolsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), readerToolsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReaderToolsDTO{" +
            "id=" + getId() +
            "}";
    }
}
