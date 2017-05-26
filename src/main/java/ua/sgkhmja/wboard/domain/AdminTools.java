package ua.sgkhmja.wboard.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AdminTools.
 */
@Entity
@Table(name = "admin_tools")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "admintools")
public class AdminTools implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private Admin admin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Admin getAdmin() {
        return admin;
    }

    public AdminTools admin(Admin admin) {
        this.admin = admin;
        return this;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdminTools adminTools = (AdminTools) o;
        if (adminTools.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), adminTools.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AdminTools{" +
            "id=" + getId() +
            "}";
    }
}
