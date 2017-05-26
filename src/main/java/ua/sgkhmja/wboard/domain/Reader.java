package ua.sgkhmja.wboard.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Reader.
 */
@Entity
@Table(name = "reader")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reader")
public class Reader implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private BoardUser boardUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BoardUser getBoardUser() {
        return boardUser;
    }

    public Reader boardUser(BoardUser boardUser) {
        this.boardUser = boardUser;
        return this;
    }

    public void setBoardUser(BoardUser boardUser) {
        this.boardUser = boardUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reader reader = (Reader) o;
        if (reader.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reader.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Reader{" +
            "id=" + getId() +
            "}";
    }
}
