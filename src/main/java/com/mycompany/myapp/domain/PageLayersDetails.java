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
 * A PageLayersDetails.
 */
@Entity
@Table(name = "page_layers_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PageLayersDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToMany(mappedBy = "pageElementDetails")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pageElementDetails", "booksPages" }, allowSetters = true)
    private Set<PageLayers> pageElements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PageLayersDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PageLayersDetails name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public PageLayersDetails description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public PageLayersDetails isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<PageLayers> getPageElements() {
        return this.pageElements;
    }

    public void setPageElements(Set<PageLayers> pageLayers) {
        if (this.pageElements != null) {
            this.pageElements.forEach(i -> i.removePageElementDetails(this));
        }
        if (pageLayers != null) {
            pageLayers.forEach(i -> i.addPageElementDetails(this));
        }
        this.pageElements = pageLayers;
    }

    public PageLayersDetails pageElements(Set<PageLayers> pageLayers) {
        this.setPageElements(pageLayers);
        return this;
    }

    public PageLayersDetails addPageElement(PageLayers pageLayers) {
        this.pageElements.add(pageLayers);
        pageLayers.getPageElementDetails().add(this);
        return this;
    }

    public PageLayersDetails removePageElement(PageLayers pageLayers) {
        this.pageElements.remove(pageLayers);
        pageLayers.getPageElementDetails().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PageLayersDetails)) {
            return false;
        }
        return id != null && id.equals(((PageLayersDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PageLayersDetails{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
