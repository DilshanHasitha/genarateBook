package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.PriceRelatedOption} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.PriceRelatedOptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /price-related-options?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PriceRelatedOptionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private BooleanFilter isActive;

    private LongFilter optionTypeId;

    private LongFilter priceRelatedOptionDetailsId;

    private LongFilter booksId;

    private Boolean distinct;

    public PriceRelatedOptionCriteria() {}

    public PriceRelatedOptionCriteria(PriceRelatedOptionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.optionTypeId = other.optionTypeId == null ? null : other.optionTypeId.copy();
        this.priceRelatedOptionDetailsId = other.priceRelatedOptionDetailsId == null ? null : other.priceRelatedOptionDetailsId.copy();
        this.booksId = other.booksId == null ? null : other.booksId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PriceRelatedOptionCriteria copy() {
        return new PriceRelatedOptionCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getOptionTypeId() {
        return optionTypeId;
    }

    public LongFilter optionTypeId() {
        if (optionTypeId == null) {
            optionTypeId = new LongFilter();
        }
        return optionTypeId;
    }

    public void setOptionTypeId(LongFilter optionTypeId) {
        this.optionTypeId = optionTypeId;
    }

    public LongFilter getPriceRelatedOptionDetailsId() {
        return priceRelatedOptionDetailsId;
    }

    public LongFilter priceRelatedOptionDetailsId() {
        if (priceRelatedOptionDetailsId == null) {
            priceRelatedOptionDetailsId = new LongFilter();
        }
        return priceRelatedOptionDetailsId;
    }

    public void setPriceRelatedOptionDetailsId(LongFilter priceRelatedOptionDetailsId) {
        this.priceRelatedOptionDetailsId = priceRelatedOptionDetailsId;
    }

    public LongFilter getBooksId() {
        return booksId;
    }

    public LongFilter booksId() {
        if (booksId == null) {
            booksId = new LongFilter();
        }
        return booksId;
    }

    public void setBooksId(LongFilter booksId) {
        this.booksId = booksId;
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
        final PriceRelatedOptionCriteria that = (PriceRelatedOptionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(optionTypeId, that.optionTypeId) &&
            Objects.equals(priceRelatedOptionDetailsId, that.priceRelatedOptionDetailsId) &&
            Objects.equals(booksId, that.booksId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, isActive, optionTypeId, priceRelatedOptionDetailsId, booksId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PriceRelatedOptionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (optionTypeId != null ? "optionTypeId=" + optionTypeId + ", " : "") +
            (priceRelatedOptionDetailsId != null ? "priceRelatedOptionDetailsId=" + priceRelatedOptionDetailsId + ", " : "") +
            (booksId != null ? "booksId=" + booksId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
