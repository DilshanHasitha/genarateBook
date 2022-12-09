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
 * A LayerGroup.
 */
@Entity
@Table(name = "layer_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LayerGroup implements Serializable {

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "rel_layer_group__layers",
        joinColumns = @JoinColumn(name = "layer_group_id"),
        inverseJoinColumns = @JoinColumn(name = "layers_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "layerdetails", "layerGroups" }, allowSetters = true)
    private Set<Layers> layers = new HashSet<>();

    @ManyToMany(mappedBy = "layerGroups")
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

    public LayerGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public LayerGroup code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public LayerGroup description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public LayerGroup isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Layers> getLayers() {
        return this.layers;
    }

    public void setLayers(Set<Layers> layers) {
        this.layers = layers;
    }

    public LayerGroup layers(Set<Layers> layers) {
        this.setLayers(layers);
        return this;
    }

    public LayerGroup addLayers(Layers layers) {
        this.layers.add(layers);
        layers.getLayerGroups().add(this);
        return this;
    }

    public LayerGroup removeLayers(Layers layers) {
        this.layers.remove(layers);
        layers.getLayerGroups().remove(this);
        return this;
    }

    public Set<Books> getBooks() {
        return this.books;
    }

    public void setBooks(Set<Books> books) {
        if (this.books != null) {
            this.books.forEach(i -> i.removeLayerGroup(this));
        }
        if (books != null) {
            books.forEach(i -> i.addLayerGroup(this));
        }
        this.books = books;
    }

    public LayerGroup books(Set<Books> books) {
        this.setBooks(books);
        return this;
    }

    public LayerGroup addBooks(Books books) {
        this.books.add(books);
        books.getLayerGroups().add(this);
        return this;
    }

    public LayerGroup removeBooks(Books books) {
        this.books.remove(books);
        books.getLayerGroups().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LayerGroup)) {
            return false;
        }
        return id != null && id.equals(((LayerGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LayerGroup{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
