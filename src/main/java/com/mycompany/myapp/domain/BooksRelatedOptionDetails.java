package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BooksRelatedOptionDetails.
 */
@Entity
@Table(name = "books_related_option_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BooksRelatedOptionDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToMany(mappedBy = "booksRelatedOptionDetails")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "booksRelatedOptionDetails", "books" }, allowSetters = true)
    private Set<BooksRelatedOption> booksRelatedOptions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BooksRelatedOptionDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public BooksRelatedOptionDetails code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public BooksRelatedOptionDetails description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public BooksRelatedOptionDetails isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<BooksRelatedOption> getBooksRelatedOptions() {
        return this.booksRelatedOptions;
    }

    public void setBooksRelatedOptions(Set<BooksRelatedOption> booksRelatedOptions) {
        if (this.booksRelatedOptions != null) {
            this.booksRelatedOptions.forEach(i -> i.removeBooksRelatedOptionDetails(this));
        }
        if (booksRelatedOptions != null) {
            booksRelatedOptions.forEach(i -> i.addBooksRelatedOptionDetails(this));
        }
        this.booksRelatedOptions = booksRelatedOptions;
    }

    public BooksRelatedOptionDetails booksRelatedOptions(Set<BooksRelatedOption> booksRelatedOptions) {
        this.setBooksRelatedOptions(booksRelatedOptions);
        return this;
    }

    public BooksRelatedOptionDetails addBooksRelatedOption(BooksRelatedOption booksRelatedOption) {
        this.booksRelatedOptions.add(booksRelatedOption);
        booksRelatedOption.getBooksRelatedOptionDetails().add(this);
        return this;
    }

    public BooksRelatedOptionDetails removeBooksRelatedOption(BooksRelatedOption booksRelatedOption) {
        this.booksRelatedOptions.remove(booksRelatedOption);
        booksRelatedOption.getBooksRelatedOptionDetails().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BooksRelatedOptionDetails)) {
            return false;
        }
        return id != null && id.equals(((BooksRelatedOptionDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BooksRelatedOptionDetails{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
