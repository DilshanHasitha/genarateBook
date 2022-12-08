package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.BooksOptionDetails} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.BooksOptionDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /books-option-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BooksOptionDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter avatarAttributes;

    private StringFilter avatarCharactor;

    private StringFilter style;

    private StringFilter option;

    private BooleanFilter isActive;

    private LongFilter booksId;

    private Boolean distinct;

    public BooksOptionDetailsCriteria() {}

    public BooksOptionDetailsCriteria(BooksOptionDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.avatarAttributes = other.avatarAttributes == null ? null : other.avatarAttributes.copy();
        this.avatarCharactor = other.avatarCharactor == null ? null : other.avatarCharactor.copy();
        this.style = other.style == null ? null : other.style.copy();
        this.option = other.option == null ? null : other.option.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.booksId = other.booksId == null ? null : other.booksId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BooksOptionDetailsCriteria copy() {
        return new BooksOptionDetailsCriteria(this);
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

    public StringFilter getAvatarAttributes() {
        return avatarAttributes;
    }

    public StringFilter avatarAttributes() {
        if (avatarAttributes == null) {
            avatarAttributes = new StringFilter();
        }
        return avatarAttributes;
    }

    public void setAvatarAttributes(StringFilter avatarAttributes) {
        this.avatarAttributes = avatarAttributes;
    }

    public StringFilter getAvatarCharactor() {
        return avatarCharactor;
    }

    public StringFilter avatarCharactor() {
        if (avatarCharactor == null) {
            avatarCharactor = new StringFilter();
        }
        return avatarCharactor;
    }

    public void setAvatarCharactor(StringFilter avatarCharactor) {
        this.avatarCharactor = avatarCharactor;
    }

    public StringFilter getStyle() {
        return style;
    }

    public StringFilter style() {
        if (style == null) {
            style = new StringFilter();
        }
        return style;
    }

    public void setStyle(StringFilter style) {
        this.style = style;
    }

    public StringFilter getOption() {
        return option;
    }

    public StringFilter option() {
        if (option == null) {
            option = new StringFilter();
        }
        return option;
    }

    public void setOption(StringFilter option) {
        this.option = option;
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
        final BooksOptionDetailsCriteria that = (BooksOptionDetailsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(avatarAttributes, that.avatarAttributes) &&
            Objects.equals(avatarCharactor, that.avatarCharactor) &&
            Objects.equals(style, that.style) &&
            Objects.equals(option, that.option) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(booksId, that.booksId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, avatarAttributes, avatarCharactor, style, option, isActive, booksId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BooksOptionDetailsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (avatarAttributes != null ? "avatarAttributes=" + avatarAttributes + ", " : "") +
            (avatarCharactor != null ? "avatarCharactor=" + avatarCharactor + ", " : "") +
            (style != null ? "style=" + style + ", " : "") +
            (option != null ? "option=" + option + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (booksId != null ? "booksId=" + booksId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
