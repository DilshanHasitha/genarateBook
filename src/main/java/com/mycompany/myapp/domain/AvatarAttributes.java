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
 * A AvatarAttributes.
 */
@Entity
@Table(name = "avatar_attributes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AvatarAttributes implements Serializable {

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

    @ManyToMany
    @JoinTable(
        name = "rel_avatar_attributes__avatar_charactor",
        joinColumns = @JoinColumn(name = "avatar_attributes_id"),
        inverseJoinColumns = @JoinColumn(name = "avatar_charactor_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "avatarAttributes" }, allowSetters = true)
    private Set<AvatarCharactor> avatarCharactors = new HashSet<>();

    @ManyToMany(mappedBy = "avatarAttributes")
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
        },
        allowSetters = true
    )
    private Set<Books> books = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_avatar_attributes__styles",
        joinColumns = @JoinColumn(name = "avatar_attributes_id"),
        inverseJoinColumns = @JoinColumn(name = "styles_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "options" }, allowSetters = true)
    private Set<Styles> styles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AvatarAttributes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public AvatarAttributes code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public AvatarAttributes description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public AvatarAttributes isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<AvatarCharactor> getAvatarCharactors() {
        return this.avatarCharactors;
    }

    public void setAvatarCharactors(Set<AvatarCharactor> avatarCharactors) {
        this.avatarCharactors = avatarCharactors;
    }

    public AvatarAttributes avatarCharactors(Set<AvatarCharactor> avatarCharactors) {
        this.setAvatarCharactors(avatarCharactors);
        return this;
    }

    public AvatarAttributes addAvatarCharactor(AvatarCharactor avatarCharactor) {
        this.avatarCharactors.add(avatarCharactor);
        avatarCharactor.getAvatarAttributes().add(this);
        return this;
    }

    public AvatarAttributes removeAvatarCharactor(AvatarCharactor avatarCharactor) {
        this.avatarCharactors.remove(avatarCharactor);
        avatarCharactor.getAvatarAttributes().remove(this);
        return this;
    }

    public Set<Books> getBooks() {
        return this.books;
    }

    public void setBooks(Set<Books> books) {
        if (this.books != null) {
            this.books.forEach(i -> i.removeAvatarAttributes(this));
        }
        if (books != null) {
            books.forEach(i -> i.addAvatarAttributes(this));
        }
        this.books = books;
    }

    public AvatarAttributes books(Set<Books> books) {
        this.setBooks(books);
        return this;
    }

    public AvatarAttributes addBooks(Books books) {
        this.books.add(books);
        books.getAvatarAttributes().add(this);
        return this;
    }

    public AvatarAttributes removeBooks(Books books) {
        this.books.remove(books);
        books.getAvatarAttributes().remove(this);
        return this;
    }

    public Set<Styles> getStyles() {
        return this.styles;
    }

    public void setStyles(Set<Styles> styles) {
        this.styles = styles;
    }

    public AvatarAttributes styles(Set<Styles> styles) {
        this.setStyles(styles);
        return this;
    }

    public AvatarAttributes addStyles(Styles styles) {
        this.styles.add(styles);
        return this;
    }

    public AvatarAttributes removeStyles(Styles styles) {
        this.styles.remove(styles);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AvatarAttributes)) {
            return false;
        }
        return id != null && id.equals(((AvatarAttributes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AvatarAttributes{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
