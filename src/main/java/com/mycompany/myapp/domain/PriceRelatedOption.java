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
 * A PriceRelatedOption.
 */
@Entity
@Table(name = "price_related_option")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PriceRelatedOption implements Serializable {

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

    @ManyToOne
    private OptionType optionType;

    @ManyToMany
    @JoinTable(
        name = "rel_price_related_option__price_related_option_details",
        joinColumns = @JoinColumn(name = "price_related_option_id"),
        inverseJoinColumns = @JoinColumn(name = "price_related_option_details_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "priceRelatedOptions" }, allowSetters = true)
    private Set<PriceRelatedOptionDetails> priceRelatedOptionDetails = new HashSet<>();

    @ManyToMany(mappedBy = "priceRelatedOptions")
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

    public PriceRelatedOption id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public PriceRelatedOption code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public PriceRelatedOption name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public PriceRelatedOption isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public OptionType getOptionType() {
        return this.optionType;
    }

    public void setOptionType(OptionType optionType) {
        this.optionType = optionType;
    }

    public PriceRelatedOption optionType(OptionType optionType) {
        this.setOptionType(optionType);
        return this;
    }

    public Set<PriceRelatedOptionDetails> getPriceRelatedOptionDetails() {
        return this.priceRelatedOptionDetails;
    }

    public void setPriceRelatedOptionDetails(Set<PriceRelatedOptionDetails> priceRelatedOptionDetails) {
        this.priceRelatedOptionDetails = priceRelatedOptionDetails;
    }

    public PriceRelatedOption priceRelatedOptionDetails(Set<PriceRelatedOptionDetails> priceRelatedOptionDetails) {
        this.setPriceRelatedOptionDetails(priceRelatedOptionDetails);
        return this;
    }

    public PriceRelatedOption addPriceRelatedOptionDetails(PriceRelatedOptionDetails priceRelatedOptionDetails) {
        this.priceRelatedOptionDetails.add(priceRelatedOptionDetails);
        priceRelatedOptionDetails.getPriceRelatedOptions().add(this);
        return this;
    }

    public PriceRelatedOption removePriceRelatedOptionDetails(PriceRelatedOptionDetails priceRelatedOptionDetails) {
        this.priceRelatedOptionDetails.remove(priceRelatedOptionDetails);
        priceRelatedOptionDetails.getPriceRelatedOptions().remove(this);
        return this;
    }

    public Set<Books> getBooks() {
        return this.books;
    }

    public void setBooks(Set<Books> books) {
        if (this.books != null) {
            this.books.forEach(i -> i.removePriceRelatedOption(this));
        }
        if (books != null) {
            books.forEach(i -> i.addPriceRelatedOption(this));
        }
        this.books = books;
    }

    public PriceRelatedOption books(Set<Books> books) {
        this.setBooks(books);
        return this;
    }

    public PriceRelatedOption addBooks(Books books) {
        this.books.add(books);
        books.getPriceRelatedOptions().add(this);
        return this;
    }

    public PriceRelatedOption removeBooks(Books books) {
        this.books.remove(books);
        books.getPriceRelatedOptions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PriceRelatedOption)) {
            return false;
        }
        return id != null && id.equals(((PriceRelatedOption) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PriceRelatedOption{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
