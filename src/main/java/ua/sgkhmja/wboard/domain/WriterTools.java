package ua.sgkhmja.wboard.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A WriterTools.
 */
@Entity
@Table(name = "writer_tools")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "writertools")
public class WriterTools implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private Writer writer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Writer getWriter() {
        return writer;
    }

    public WriterTools writer(Writer writer) {
        this.writer = writer;
        return this;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WriterTools writerTools = (WriterTools) o;
        if (writerTools.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), writerTools.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WriterTools{" +
            "id=" + getId() +
            "}";
    }
}
