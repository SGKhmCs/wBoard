package ua.sgkhmja.wboard.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A BoardsBody.
 */
@Entity
@Table(name = "boards_body")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "boardsbody")
public class BoardsBody implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "background_color")
    private Integer backgroundColor;

    @OneToOne
    @JoinColumn(unique = true)
    private Board board;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBackgroundColor() {
        return backgroundColor;
    }

    public BoardsBody backgroundColor(Integer backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public void setBackgroundColor(Integer backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Board getBoard() {
        return board;
    }

    public BoardsBody board(Board board) {
        this.board = board;
        return this;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoardsBody boardsBody = (BoardsBody) o;
        if (boardsBody.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), boardsBody.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BoardsBody{" +
            "id=" + getId() +
            ", backgroundColor='" + getBackgroundColor() + "'" +
            "}";
    }
}
