package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.ImageStoreType} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ImageStoreTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /image-store-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageStoreTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter imageStoreTypeCode;

    private StringFilter imageStoreTypeDescription;

    private BooleanFilter isActive;

    private Boolean distinct;

    public ImageStoreTypeCriteria() {}

    public ImageStoreTypeCriteria(ImageStoreTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.imageStoreTypeCode = other.imageStoreTypeCode == null ? null : other.imageStoreTypeCode.copy();
        this.imageStoreTypeDescription = other.imageStoreTypeDescription == null ? null : other.imageStoreTypeDescription.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ImageStoreTypeCriteria copy() {
        return new ImageStoreTypeCriteria(this);
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

    public StringFilter getImageStoreTypeCode() {
        return imageStoreTypeCode;
    }

    public StringFilter imageStoreTypeCode() {
        if (imageStoreTypeCode == null) {
            imageStoreTypeCode = new StringFilter();
        }
        return imageStoreTypeCode;
    }

    public void setImageStoreTypeCode(StringFilter imageStoreTypeCode) {
        this.imageStoreTypeCode = imageStoreTypeCode;
    }

    public StringFilter getImageStoreTypeDescription() {
        return imageStoreTypeDescription;
    }

    public StringFilter imageStoreTypeDescription() {
        if (imageStoreTypeDescription == null) {
            imageStoreTypeDescription = new StringFilter();
        }
        return imageStoreTypeDescription;
    }

    public void setImageStoreTypeDescription(StringFilter imageStoreTypeDescription) {
        this.imageStoreTypeDescription = imageStoreTypeDescription;
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
        final ImageStoreTypeCriteria that = (ImageStoreTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(imageStoreTypeCode, that.imageStoreTypeCode) &&
            Objects.equals(imageStoreTypeDescription, that.imageStoreTypeDescription) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imageStoreTypeCode, imageStoreTypeDescription, isActive, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageStoreTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (imageStoreTypeCode != null ? "imageStoreTypeCode=" + imageStoreTypeCode + ", " : "") +
            (imageStoreTypeDescription != null ? "imageStoreTypeDescription=" + imageStoreTypeDescription + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
