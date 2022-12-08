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
 * A Layers.
 */
@Entity
@Table(name = "layers")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Layers implements Serializable {

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
        name = "rel_layers__layerdetails",
        joinColumns = @JoinColumn(name = "layers_id"),
        inverseJoinColumns = @JoinColumn(name = "layerdetails_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "layers" }, allowSetters = true)
    private Set<LayerDetails> layerdetails = new HashSet<>();

    @ManyToMany(mappedBy = "layers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "layers", "books" }, allowSetters = true)
    private Set<LayerGroup> layerGroups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Layers id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLayerNo() {
        return this.layerNo;
    }

    public Layers layerNo(Integer layerNo) {
        this.setLayerNo(layerNo);
        return this;
    }

    public void setLayerNo(Integer layerNo) {
        this.layerNo = layerNo;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Layers isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<LayerDetails> getLayerdetails() {
        return this.layerdetails;
    }

    public void setLayerdetails(Set<LayerDetails> layerDetails) {
        this.layerdetails = layerDetails;
    }

    public Layers layerdetails(Set<LayerDetails> layerDetails) {
        this.setLayerdetails(layerDetails);
        return this;
    }

    public Layers addLayerdetails(LayerDetails layerDetails) {
        this.layerdetails.add(layerDetails);
        layerDetails.getLayers().add(this);
        return this;
    }

    public Layers removeLayerdetails(LayerDetails layerDetails) {
        this.layerdetails.remove(layerDetails);
        layerDetails.getLayers().remove(this);
        return this;
    }

    public Set<LayerGroup> getLayerGroups() {
        return this.layerGroups;
    }

    public void setLayerGroups(Set<LayerGroup> layerGroups) {
        if (this.layerGroups != null) {
            this.layerGroups.forEach(i -> i.removeLayers(this));
        }
        if (layerGroups != null) {
            layerGroups.forEach(i -> i.addLayers(this));
        }
        this.layerGroups = layerGroups;
    }

    public Layers layerGroups(Set<LayerGroup> layerGroups) {
        this.setLayerGroups(layerGroups);
        return this;
    }

    public Layers addLayerGroup(LayerGroup layerGroup) {
        this.layerGroups.add(layerGroup);
        layerGroup.getLayers().add(this);
        return this;
    }

    public Layers removeLayerGroup(LayerGroup layerGroup) {
        this.layerGroups.remove(layerGroup);
        layerGroup.getLayers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Layers)) {
            return false;
        }
        return id != null && id.equals(((Layers) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Layers{" +
            "id=" + getId() +
            ", layerNo=" + getLayerNo() +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
