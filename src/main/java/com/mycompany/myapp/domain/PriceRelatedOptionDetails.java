package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PriceRelatedOptionDetails.
 */
@Entity
@Table(name = "price_related_option_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PriceRelatedOptionDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @ManyToMany(mappedBy = "priceRelatedOptionDetails")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "optionType", "priceRelatedOptionDetails", "books" }, allowSetters = true)
    private Set<PriceRelatedOption> priceRelatedOptions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PriceRelatedOptionDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public PriceRelatedOptionDetails description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public PriceRelatedOptionDetails price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<PriceRelatedOption> getPriceRelatedOptions() {
        return this.priceRelatedOptions;
    }

    public void setPriceRelatedOptions(Set<PriceRelatedOption> priceRelatedOptions) {
        if (this.priceRelatedOptions != null) {
            this.priceRelatedOptions.forEach(i -> i.removePriceRelatedOptionDetails(this));
        }
        if (priceRelatedOptions != null) {
            priceRelatedOptions.forEach(i -> i.addPriceRelatedOptionDetails(this));
        }
        this.priceRelatedOptions = priceRelatedOptions;
    }

    public PriceRelatedOptionDetails priceRelatedOptions(Set<PriceRelatedOption> priceRelatedOptions) {
        this.setPriceRelatedOptions(priceRelatedOptions);
        return this;
    }

    public PriceRelatedOptionDetails addPriceRelatedOption(PriceRelatedOption priceRelatedOption) {
        this.priceRelatedOptions.add(priceRelatedOption);
        priceRelatedOption.getPriceRelatedOptionDetails().add(this);
        return this;
    }

    public PriceRelatedOptionDetails removePriceRelatedOption(PriceRelatedOption priceRelatedOption) {
        this.priceRelatedOptions.remove(priceRelatedOption);
        priceRelatedOption.getPriceRelatedOptionDetails().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PriceRelatedOptionDetails)) {
            return false;
        }
        return id != null && id.equals(((PriceRelatedOptionDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PriceRelatedOptionDetails{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
