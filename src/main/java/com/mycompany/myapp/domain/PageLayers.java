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
 * A PageLayers.
 */
@Entity
@Table(name = "page_layers")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PageLayers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "layer_no", nullable = false)
    private Integer layerNo;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "rel_page_layers__page_element_details",
        joinColumns = @JoinColumn(name = "page_layers_id"),
        inverseJoinColumns = @JoinColumn(name = "page_element_details_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pageElements" }, allowSetters = true)
    private Set<PageLayersDetails> pageElementDetails = new HashSet<>();

    @ManyToMany(mappedBy = "pageDetails")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pageDetails", "books" }, allowSetters = true)
    private Set<BooksPage> booksPages = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PageLayers id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLayerNo() {
        return this.layerNo;
    }

    public PageLayers layerNo(Integer layerNo) {
        this.setLayerNo(layerNo);
        return this;
    }

    public void setLayerNo(Integer layerNo) {
        this.layerNo = layerNo;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public PageLayers isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<PageLayersDetails> getPageElementDetails() {
        return this.pageElementDetails;
    }

    public void setPageElementDetails(Set<PageLayersDetails> pageLayersDetails) {
        this.pageElementDetails = pageLayersDetails;
    }

    public PageLayers pageElementDetails(Set<PageLayersDetails> pageLayersDetails) {
        this.setPageElementDetails(pageLayersDetails);
        return this;
    }

    public PageLayers addPageElementDetails(PageLayersDetails pageLayersDetails) {
        this.pageElementDetails.add(pageLayersDetails);
        pageLayersDetails.getPageElements().add(this);
        return this;
    }

    public PageLayers removePageElementDetails(PageLayersDetails pageLayersDetails) {
        this.pageElementDetails.remove(pageLayersDetails);
        pageLayersDetails.getPageElements().remove(this);
        return this;
    }

    public Set<BooksPage> getBooksPages() {
        return this.booksPages;
    }

    public void setBooksPages(Set<BooksPage> booksPages) {
        if (this.booksPages != null) {
            this.booksPages.forEach(i -> i.removePageDetails(this));
        }
        if (booksPages != null) {
            booksPages.forEach(i -> i.addPageDetails(this));
        }
        this.booksPages = booksPages;
    }

    public PageLayers booksPages(Set<BooksPage> booksPages) {
        this.setBooksPages(booksPages);
        return this;
    }

    public PageLayers addBooksPage(BooksPage booksPage) {
        this.booksPages.add(booksPage);
        booksPage.getPageDetails().add(this);
        return this;
    }

    public PageLayers removeBooksPage(BooksPage booksPage) {
        this.booksPages.remove(booksPage);
        booksPage.getPageDetails().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PageLayers)) {
            return false;
        }
        return id != null && id.equals(((PageLayers) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PageLayers{" +
            "id=" + getId() +
            ", layerNo=" + getLayerNo() +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
