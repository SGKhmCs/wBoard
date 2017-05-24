package ua.sgkhmja.wboard.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
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

    @NotNull
    @Column(name = "pub", nullable = false)
    private Boolean pub;

    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "board")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Reader> readers = new HashSet<>();

    @OneToMany(mappedBy = "board")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Writer> writers = new HashSet<>();

    @OneToMany(mappedBy = "board")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Admin> admins = new HashSet<>();

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

    public User getOwner() {
        return owner;
    }

    public Board owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Set<Reader> getReaders() {
        return readers;
    }

    public Board readers(Set<Reader> readers) {
        this.readers = readers;
        return this;
    }

    public Board addReader(Reader reader) {
        this.readers.add(reader);
        reader.setBoard(this);
        return this;
    }

    public Board removeReader(Reader reader) {
        this.readers.remove(reader);
        reader.setBoard(null);
        return this;
    }

    public void setReaders(Set<Reader> readers) {
        this.readers = readers;
    }

    public Set<Writer> getWriters() {
        return writers;
    }

    public Board writers(Set<Writer> writers) {
        this.writers = writers;
        return this;
    }

    public Board addWriter(Writer writer) {
        this.writers.add(writer);
        writer.setBoard(this);
        return this;
    }

    public Board removeWriter(Writer writer) {
        this.writers.remove(writer);
        writer.setBoard(null);
        return this;
    }

    public void setWriters(Set<Writer> writers) {
        this.writers = writers;
    }

    public Set<Admin> getAdmins() {
        return admins;
    }

    public Board admins(Set<Admin> admins) {
        this.admins = admins;
        return this;
    }

    public Board addAdmin(Admin admin) {
        this.admins.add(admin);
        admin.setBoard(this);
        return this;
    }

    public Board removeAdmin(Admin admin) {
        this.admins.remove(admin);
        admin.setBoard(null);
        return this;
    }

    public void setAdmins(Set<Admin> admins) {
        this.admins = admins;
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
            "}";
    }
}
