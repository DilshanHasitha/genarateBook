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

    private IntegerFilter width;

    private IntegerFilter height;

    private IntegerFilter x;

    private IntegerFilter y;

    private BooleanFilter isText;

    private LongFilter stylesDetailsId;

    private Boolean distinct;

    public StylesCriteria() {}

    public StylesCriteria(StylesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.imgURL = other.imgURL == null ? null : other.imgURL.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.width = other.width == null ? null : other.width.copy();
        this.height = other.height == null ? null : other.height.copy();
        this.x = other.x == null ? null : other.x.copy();
        this.y = other.y == null ? null : other.y.copy();
        this.isText = other.isText == null ? null : other.isText.copy();
        this.stylesDetailsId = other.stylesDetailsId == null ? null : other.stylesDetailsId.copy();
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

    public IntegerFilter getWidth() {
        return width;
    }

    public IntegerFilter width() {
        if (width == null) {
            width = new IntegerFilter();
        }
        return width;
    }

    public void setWidth(IntegerFilter width) {
        this.width = width;
    }

    public IntegerFilter getHeight() {
        return height;
    }

    public IntegerFilter height() {
        if (height == null) {
            height = new IntegerFilter();
        }
        return height;
    }

    public void setHeight(IntegerFilter height) {
        this.height = height;
    }

    public IntegerFilter getX() {
        return x;
    }

    public IntegerFilter x() {
        if (x == null) {
            x = new IntegerFilter();
        }
        return x;
    }

    public void setX(IntegerFilter x) {
        this.x = x;
    }

    public IntegerFilter getY() {
        return y;
    }

    public IntegerFilter y() {
        if (y == null) {
            y = new IntegerFilter();
        }
        return y;
    }

    public void setY(IntegerFilter y) {
        this.y = y;
    }

    public BooleanFilter getIsText() {
        return isText;
    }

    public BooleanFilter isText() {
        if (isText == null) {
            isText = new BooleanFilter();
        }
        return isText;
    }

    public void setIsText(BooleanFilter isText) {
        this.isText = isText;
    }

    public LongFilter getStylesDetailsId() {
        return stylesDetailsId;
    }

    public LongFilter stylesDetailsId() {
        if (stylesDetailsId == null) {
            stylesDetailsId = new LongFilter();
        }
        return stylesDetailsId;
    }

    public void setStylesDetailsId(LongFilter stylesDetailsId) {
        this.stylesDetailsId = stylesDetailsId;
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
            Objects.equals(width, that.width) &&
            Objects.equals(height, that.height) &&
            Objects.equals(x, that.x) &&
            Objects.equals(y, that.y) &&
            Objects.equals(isText, that.isText) &&
            Objects.equals(stylesDetailsId, that.stylesDetailsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, description, imgURL, isActive, width, height, x, y, isText, stylesDetailsId, distinct);
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
            (width != null ? "width=" + width + ", " : "") +
            (height != null ? "height=" + height + ", " : "") +
            (x != null ? "x=" + x + ", " : "") +
            (y != null ? "y=" + y + ", " : "") +
            (isText != null ? "isText=" + isText + ", " : "") +
            (stylesDetailsId != null ? "stylesDetailsId=" + stylesDetailsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
