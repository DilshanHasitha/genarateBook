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
 * A BooksPage.
 */
@Entity
@Table(name = "books_page")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BooksPage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "num", nullable = false)
    private Integer num;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "rel_books_page__page_details",
        joinColumns = @JoinColumn(name = "books_page_id"),
        inverseJoinColumns = @JoinColumn(name = "page_details_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pageElementDetails", "booksPages" }, allowSetters = true)
    private Set<PageLayers> pageDetails = new HashSet<>();

    @ManyToMany(mappedBy = "booksPages")
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

    public BooksPage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNum() {
        return this.num;
    }

    public BooksPage num(Integer num) {
        this.setNum(num);
        return this;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public BooksPage isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<PageLayers> getPageDetails() {
        return this.pageDetails;
    }

    public void setPageDetails(Set<PageLayers> pageLayers) {
        this.pageDetails = pageLayers;
    }

    public BooksPage pageDetails(Set<PageLayers> pageLayers) {
        this.setPageDetails(pageLayers);
        return this;
    }

    public BooksPage addPageDetails(PageLayers pageLayers) {
        this.pageDetails.add(pageLayers);
        pageLayers.getBooksPages().add(this);
        return this;
    }

    public BooksPage removePageDetails(PageLayers pageLayers) {
        this.pageDetails.remove(pageLayers);
        pageLayers.getBooksPages().remove(this);
        return this;
    }

    public Set<Books> getBooks() {
        return this.books;
    }

    public void setBooks(Set<Books> books) {
        if (this.books != null) {
            this.books.forEach(i -> i.removeBooksPage(this));
        }
        if (books != null) {
            books.forEach(i -> i.addBooksPage(this));
        }
        this.books = books;
    }

    public BooksPage books(Set<Books> books) {
        this.setBooks(books);
        return this;
    }

    public BooksPage addBooks(Books books) {
        this.books.add(books);
        books.getBooksPages().add(this);
        return this;
    }

    public BooksPage removeBooks(Books books) {
        this.books.remove(books);
        books.getBooksPages().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BooksPage)) {
            return false;
        }
        return id != null && id.equals(((BooksPage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BooksPage{" +
            "id=" + getId() +
            ", num=" + getNum() +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
