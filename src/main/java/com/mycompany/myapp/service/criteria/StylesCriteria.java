package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Styles} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.StylesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /styles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StylesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter description;

    private StringFilter imgURL;

    private BooleanFilter isActive;

    private LongFilter optionId;

    private Boolean distinct;

    public StylesCriteria() {}

    public StylesCriteria(StylesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.imgURL = other.imgURL == null ? null : other.imgURL.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.optionId = other.optionId == null ? null : other.optionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public StylesCriteria copy() {
        return new StylesCriteria(this);
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

    public StringFilter getImgURL() {
        return imgURL;
    }

    public StringFilter imgURL() {
        if (imgURL == null) {
            imgURL = new StringFilter();
        }
        return imgURL;
    }

    public void setImgURL(StringFilter imgURL) {
        this.imgURL = imgURL;
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

    public LongFilter getOptionId() {
        return optionId;
    }

    public LongFilter optionId() {
        if (optionId == null) {
            optionId = new LongFilter();
        }
        return optionId;
    }

    public void setOptionId(LongFilter optionId) {
        this.optionId = optionId;
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
        final StylesCriteria that = (StylesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(description, that.description) &&
            Objects.equals(imgURL, that.imgURL) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(optionId, that.optionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, description, imgURL, isActive, optionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StylesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (imgURL != null ? "imgURL=" + imgURL + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (optionId != null ? "optionId=" + optionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
