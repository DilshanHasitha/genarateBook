package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.PriceRelatedOptionDetails} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.PriceRelatedOptionDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /price-related-option-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PriceRelatedOptionDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private BigDecimalFilter price;

    private LongFilter priceRelatedOptionId;

    private Boolean distinct;

    public PriceRelatedOptionDetailsCriteria() {}

    public PriceRelatedOptionDetailsCriteria(PriceRelatedOptionDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.priceRelatedOptionId = other.priceRelatedOptionId == null ? null : other.priceRelatedOptionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PriceRelatedOptionDetailsCriteria copy() {
        return new PriceRelatedOptionDetailsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public BigDecimalFilter price() {
        if (price == null) {
            price = new BigDecimalFilter();
        }
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public LongFilter getPriceRelatedOptionId() {
        return priceRelatedOptionId;
    }

    public LongFilter priceRelatedOptionId() {
        if (priceRelatedOptionId == null) {
            priceRelatedOptionId = new LongFilter();
        }
        return priceRelatedOptionId;
    }

    public void setPriceRelatedOptionId(LongFilter priceRelatedOptionId) {
        this.priceRelatedOptionId = priceRelatedOptionId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PriceRelatedOptionDetailsCriteria that = (PriceRelatedOptionDetailsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(price, that.price) &&
            Objects.equals(priceRelatedOptionId, that.priceRelatedOptionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, price, priceRelatedOptionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PriceRelatedOptionDetailsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (priceRelatedOptionId != null ? "priceRelatedOptionId=" + priceRelatedOptionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
