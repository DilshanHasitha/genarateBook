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
 * A Books.
 */
@Entity
@Table(name = "books")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Books implements Serializable {

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

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "sub_title")
    private String subTitle;

    @Column(name = "author")
    private String author;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "no_of_pages")
    private Integer noOfPages;

    @Column(name = "store_img")
    private String storeImg;

    @ManyToOne
    private PageSize pageSize;

    @ManyToOne
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "rel_books__books_page",
        joinColumns = @JoinColumn(name = "books_id"),
        inverseJoinColumns = @JoinColumn(name = "books_page_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pageDetails", "books" }, allowSetters = true)
    private Set<BooksPage> booksPages = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_books__price_related_option",
        joinColumns = @JoinColumn(name = "books_id"),
        inverseJoinColumns = @JoinColumn(name = "price_related_option_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "optionType", "priceRelatedOptionDetails", "books" }, allowSetters = true)
    private Set<PriceRelatedOption> priceRelatedOptions = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_books__books_related_option",
        joinColumns = @JoinColumn(name = "books_id"),
        inverseJoinColumns = @JoinColumn(name = "books_related_option_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "booksRelatedOptionDetails", "books" }, allowSetters = true)
    private Set<BooksRelatedOption> booksRelatedOptions = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_books__books_attributes",
        joinColumns = @JoinColumn(name = "books_id"),
        inverseJoinColumns = @JoinColumn(name = "books_attributes_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "books" }, allowSetters = true)
    private Set<BooksAttributes> booksAttributes = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_books__books_variables",
        joinColumns = @JoinColumn(name = "books_id"),
        inverseJoinColumns = @JoinColumn(name = "books_variables_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "books" }, allowSetters = true)
    private Set<BooksVariables> booksVariables = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "rel_books__avatar_attributes",
        joinColumns = @JoinColumn(name = "books_id"),
        inverseJoinColumns = @JoinColumn(name = "avatar_attributes_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "avatarCharactors", "books", "styles", "options" }, allowSetters = true)
    private Set<AvatarAttributes> avatarAttributes = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_books__layer_group",
        joinColumns = @JoinColumn(name = "books_id"),
        inverseJoinColumns = @JoinColumn(name = "layer_group_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "layers", "books" }, allowSetters = true)
    private Set<LayerGroup> layerGroups = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_books__selections",
        joinColumns = @JoinColumn(name = "books_id"),
        inverseJoinColumns = @JoinColumn(name = "selections_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Selections> selections = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Books id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Books code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Books name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return this.title;
    }

    public Books title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public Books subTitle(String subTitle) {
        this.setSubTitle(subTitle);
        return this;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getAuthor() {
        return this.author;
    }

    public Books author(String author) {
        this.setAuthor(author);
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Books isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getNoOfPages() {
        return this.noOfPages;
    }

    public Books noOfPages(Integer noOfPages) {
        this.setNoOfPages(noOfPages);
        return this;
    }

    public void setNoOfPages(Integer noOfPages) {
        this.noOfPages = noOfPages;
    }

    public String getStoreImg() {
        return this.storeImg;
    }

    public Books storeImg(String storeImg) {
        this.setStoreImg(storeImg);
        return this;
    }

    public void setStoreImg(String storeImg) {
        this.storeImg = storeImg;
    }

    public PageSize getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(PageSize pageSize) {
        this.pageSize = pageSize;
    }

    public Books pageSize(PageSize pageSize) {
        this.setPageSize(pageSize);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Books user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<BooksPage> getBooksPages() {
        return this.booksPages;
    }

    public void setBooksPages(Set<BooksPage> booksPages) {
        this.booksPages = booksPages;
    }

    public Books booksPages(Set<BooksPage> booksPages) {
        this.setBooksPages(booksPages);
        return this;
    }

    public Books addBooksPage(BooksPage booksPage) {
        this.booksPages.add(booksPage);
        booksPage.getBooks().add(this);
        return this;
    }

    public Books removeBooksPage(BooksPage booksPage) {
        this.booksPages.remove(booksPage);
        booksPage.getBooks().remove(this);
        return this;
    }

    public Set<PriceRelatedOption> getPriceRelatedOptions() {
        return this.priceRelatedOptions;
    }

    public void setPriceRelatedOptions(Set<PriceRelatedOption> priceRelatedOptions) {
        this.priceRelatedOptions = priceRelatedOptions;
    }

    public Books priceRelatedOptions(Set<PriceRelatedOption> priceRelatedOptions) {
        this.setPriceRelatedOptions(priceRelatedOptions);
        return this;
    }

    public Books addPriceRelatedOption(PriceRelatedOption priceRelatedOption) {
        this.priceRelatedOptions.add(priceRelatedOption);
        priceRelatedOption.getBooks().add(this);
        return this;
    }

    public Books removePriceRelatedOption(PriceRelatedOption priceRelatedOption) {
        this.priceRelatedOptions.remove(priceRelatedOption);
        priceRelatedOption.getBooks().remove(this);
        return this;
    }

    public Set<BooksRelatedOption> getBooksRelatedOptions() {
        return this.booksRelatedOptions;
    }

    public void setBooksRelatedOptions(Set<BooksRelatedOption> booksRelatedOptions) {
        this.booksRelatedOptions = booksRelatedOptions;
    }

    public Books booksRelatedOptions(Set<BooksRelatedOption> booksRelatedOptions) {
        this.setBooksRelatedOptions(booksRelatedOptions);
        return this;
    }

    public Books addBooksRelatedOption(BooksRelatedOption booksRelatedOption) {
        this.booksRelatedOptions.add(booksRelatedOption);
        booksRelatedOption.getBooks().add(this);
        return this;
    }

    public Books removeBooksRelatedOption(BooksRelatedOption booksRelatedOption) {
        this.booksRelatedOptions.remove(booksRelatedOption);
        booksRelatedOption.getBooks().remove(this);
        return this;
    }

    public Set<BooksAttributes> getBooksAttributes() {
        return this.booksAttributes;
    }

    public void setBooksAttributes(Set<BooksAttributes> booksAttributes) {
        this.booksAttributes = booksAttributes;
    }

    public Books booksAttributes(Set<BooksAttributes> booksAttributes) {
        this.setBooksAttributes(booksAttributes);
        return this;
    }

    public Books addBooksAttributes(BooksAttributes booksAttributes) {
        this.booksAttributes.add(booksAttributes);
        booksAttributes.getBooks().add(this);
        return this;
    }

    public Books removeBooksAttributes(BooksAttributes booksAttributes) {
        this.booksAttributes.remove(booksAttributes);
        booksAttributes.getBooks().remove(this);
        return this;
    }

    public Set<BooksVariables> getBooksVariables() {
        return this.booksVariables;
    }

    public void setBooksVariables(Set<BooksVariables> booksVariables) {
        this.booksVariables = booksVariables;
    }

    public Books booksVariables(Set<BooksVariables> booksVariables) {
        this.setBooksVariables(booksVariables);
        return this;
    }

    public Books addBooksVariables(BooksVariables booksVariables) {
        this.booksVariables.add(booksVariables);
        booksVariables.getBooks().add(this);
        return this;
    }

    public Books removeBooksVariables(BooksVariables booksVariables) {
        this.booksVariables.remove(booksVariables);
        booksVariables.getBooks().remove(this);
        return this;
    }

    public Set<AvatarAttributes> getAvatarAttributes() {
        return this.avatarAttributes;
    }

    public void setAvatarAttributes(Set<AvatarAttributes> avatarAttributes) {
        this.avatarAttributes = avatarAttributes;
    }

    public Books avatarAttributes(Set<AvatarAttributes> avatarAttributes) {
        this.setAvatarAttributes(avatarAttributes);
        return this;
    }

    public Books addAvatarAttributes(AvatarAttributes avatarAttributes) {
        this.avatarAttributes.add(avatarAttributes);
        avatarAttributes.getBooks().add(this);
        return this;
    }

    public Books removeAvatarAttributes(AvatarAttributes avatarAttributes) {
        this.avatarAttributes.remove(avatarAttributes);
        avatarAttributes.getBooks().remove(this);
        return this;
    }

    public Set<LayerGroup> getLayerGroups() {
        return this.layerGroups;
    }

    public void setLayerGroups(Set<LayerGroup> layerGroups) {
        this.layerGroups = layerGroups;
    }

    public Books layerGroups(Set<LayerGroup> layerGroups) {
        this.setLayerGroups(layerGroups);
        return this;
    }

    public Books addLayerGroup(LayerGroup layerGroup) {
        this.layerGroups.add(layerGroup);
        layerGroup.getBooks().add(this);
        return this;
    }

    public Books removeLayerGroup(LayerGroup layerGroup) {
        this.layerGroups.remove(layerGroup);
        layerGroup.getBooks().remove(this);
        return this;
    }

    public Set<Selections> getSelections() {
        return this.selections;
    }

    public void setSelections(Set<Selections> selections) {
        this.selections = selections;
    }

    public Books selections(Set<Selections> selections) {
        this.setSelections(selections);
        return this;
    }

    public Books addSelections(Selections selections) {
        this.selections.add(selections);
        return this;
    }

    public Books removeSelections(Selections selections) {
        this.selections.remove(selections);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Books)) {
            return false;
        }
        return id != null && id.equals(((Books) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Books{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", title='" + getTitle() + "'" +
            ", subTitle='" + getSubTitle() + "'" +
            ", author='" + getAuthor() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", noOfPages=" + getNoOfPages() +
            ", storeImg='" + getStoreImg() + "'" +
            "}";
    }
}
