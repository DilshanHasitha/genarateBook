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
 * A BooksRelatedOption.
 */
@Entity
@Table(name = "books_related_option")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BooksRelatedOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToMany
    @JoinTable(
        name = "rel_books_related_option__books_related_option_details",
        joinColumns = @JoinColumn(name = "books_related_option_id"),
        inverseJoinColumns = @JoinColumn(name = "books_related_option_details_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "booksRelatedOptions" }, allowSetters = true)
    private Set<BooksRelatedOptionDetails> booksRelatedOptionDetails = new HashSet<>();

    @ManyToMany(mappedBy = "booksRelatedOptions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "pageSize",
            "user",
            "booksPages",
            "priceRelatedOptions",
            "booksRelatedOptions",
            "booksAttributes",
            "booksVariables",
            "avatarAttributes",
            "layerGroups",
            "selections",
        },
        allowSetters = true
    )
    private Set<Books> books = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BooksRelatedOption id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public BooksRelatedOption code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public BooksRelatedOption name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public BooksRelatedOption isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<BooksRelatedOptionDetails> getBooksRelatedOptionDetails() {
        return this.booksRelatedOptionDetails;
    }

    public void setBooksRelatedOptionDetails(Set<BooksRelatedOptionDetails> booksRelatedOptionDetails) {
        this.booksRelatedOptionDetails = booksRelatedOptionDetails;
    }

    public BooksRelatedOption booksRelatedOptionDetails(Set<BooksRelatedOptionDetails> booksRelatedOptionDetails) {
        this.setBooksRelatedOptionDetails(booksRelatedOptionDetails);
        return this;
    }

    public BooksRelatedOption addBooksRelatedOptionDetails(BooksRelatedOptionDetails booksRelatedOptionDetails) {
        this.booksRelatedOptionDetails.add(booksRelatedOptionDetails);
        booksRelatedOptionDetails.getBooksRelatedOptions().add(this);
        return this;
    }

    public BooksRelatedOption removeBooksRelatedOptionDetails(BooksRelatedOptionDetails booksRelatedOptionDetails) {
        this.booksRelatedOptionDetails.remove(booksRelatedOptionDetails);
        booksRelatedOptionDetails.getBooksRelatedOptions().remove(this);
        return this;
    }

    public Set<Books> getBooks() {
        return this.books;
    }

    public void setBooks(Set<Books> books) {
        if (this.books != null) {
            this.books.forEach(i -> i.removeBooksRelatedOption(this));
        }
        if (books != null) {
            books.forEach(i -> i.addBooksRelatedOption(this));
        }
        this.books = books;
    }

    public BooksRelatedOption books(Set<Books> books) {
        this.setBooks(books);
        return this;
    }

    public BooksRelatedOption addBooks(Books books) {
        this.books.add(books);
        books.getBooksRelatedOptions().add(this);
        return this;
    }

    public BooksRelatedOption removeBooks(Books books) {
        this.books.remove(books);
        books.getBooksRelatedOptions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BooksRelatedOption)) {
            return false;
        }
        return id != null && id.equals(((BooksRelatedOption) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BooksRelatedOption{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
