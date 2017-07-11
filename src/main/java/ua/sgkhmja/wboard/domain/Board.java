package ua.sgkhmja.wboard.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Board.
 */
@Entity
@Table(name = "board")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "board")
public class Board implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3, max = 48)
    @Column(name = "name", length = 48, nullable = false)
    private String name;

    @Column(name = "pub")
    private Boolean pub;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @OneToOne
    @JoinColumn(unique = true)
    private BoardsBody body;

    @ManyToOne
    private User createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Board name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isPub() {
        return pub;
    }

    public Board pub(Boolean pub) {
        this.pub = pub;
        return this;
    }

    public void setPub(Boolean pub) {
        this.pub = pub;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public Board createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public BoardsBody getBody() {
        return body;
    }

    public Board body(BoardsBody boardsBody) {
        this.body = boardsBody;
        return this;
    }

    public void setBody(BoardsBody boardsBody) {
        this.body = boardsBody;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public Board createdBy(User user) {
        this.createdBy = user;
        return this;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Board board = (Board) o;
        if (board.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), board.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Board{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", pub='" + isPub() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
