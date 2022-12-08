package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BooksOptionDetails.
 */
@Entity
@Table(name = "books_option_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BooksOptionDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "avatar_attributes")
    private String avatarAttributes;

    @Column(name = "avatar_charactor")
    private String avatarCharactor;

    @Column(name = "style")
    private String style;

    @Column(name = "jhi_option")
    private String option;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
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
        },
        allowSetters = true
    )
    private Books books;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BooksOptionDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatarAttributes() {
        return this.avatarAttributes;
    }

    public BooksOptionDetails avatarAttributes(String avatarAttributes) {
        this.setAvatarAttributes(avatarAttributes);
        return this;
    }

    public void setAvatarAttributes(String avatarAttributes) {
        this.avatarAttributes = avatarAttributes;
    }

    public String getAvatarCharactor() {
        return this.avatarCharactor;
    }

    public BooksOptionDetails avatarCharactor(String avatarCharactor) {
        this.setAvatarCharactor(avatarCharactor);
        return this;
    }

    public void setAvatarCharactor(String avatarCharactor) {
        this.avatarCharactor = avatarCharactor;
    }

    public String getStyle() {
        return this.style;
    }

    public BooksOptionDetails style(String style) {
        this.setStyle(style);
        return this;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getOption() {
        return this.option;
    }

    public BooksOptionDetails option(String option) {
        this.setOption(option);
        return this;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public BooksOptionDetails isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Books getBooks() {
        return this.books;
    }

    public void setBooks(Books books) {
        this.books = books;
    }

    public BooksOptionDetails books(Books books) {
        this.setBooks(books);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BooksOptionDetails)) {
            return false;
        }
        return id != null && id.equals(((BooksOptionDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BooksOptionDetails{" +
            "id=" + getId() +
            ", avatarAttributes='" + getAvatarAttributes() + "'" +
            ", avatarCharactor='" + getAvatarCharactor() + "'" +
            ", style='" + getStyle() + "'" +
            ", option='" + getOption() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
