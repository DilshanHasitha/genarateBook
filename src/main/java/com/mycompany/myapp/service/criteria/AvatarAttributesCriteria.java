package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.AvatarAttributes} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.AvatarAttributesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /avatar-attributes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AvatarAttributesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter description;

    private BooleanFilter isActive;

    private LongFilter avatarCharactorId;

    private LongFilter booksId;

    private LongFilter stylesId;

    private Boolean distinct;

    public AvatarAttributesCriteria() {}

    public AvatarAttributesCriteria(AvatarAttributesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.avatarCharactorId = other.avatarCharactorId == null ? null : other.avatarCharactorId.copy();
        this.booksId = other.booksId == null ? null : other.booksId.copy();
        this.stylesId = other.stylesId == null ? null : other.stylesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AvatarAttributesCriteria copy() {
        return new AvatarAttributesCriteria(this);
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

    public LongFilter getAvatarCharactorId() {
        return avatarCharactorId;
    }

    public LongFilter avatarCharactorId() {
        if (avatarCharactorId == null) {
            avatarCharactorId = new LongFilter();
        }
        return avatarCharactorId;
    }

    public void setAvatarCharactorId(LongFilter avatarCharactorId) {
        this.avatarCharactorId = avatarCharactorId;
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

    public LongFilter getStylesId() {
        return stylesId;
    }

    public LongFilter stylesId() {
        if (stylesId == null) {
            stylesId = new LongFilter();
        }
        return stylesId;
    }

    public void setStylesId(LongFilter stylesId) {
        this.stylesId = stylesId;
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
        final AvatarAttributesCriteria that = (AvatarAttributesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(description, that.description) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(avatarCharactorId, that.avatarCharactorId) &&
            Objects.equals(booksId, that.booksId) &&
            Objects.equals(stylesId, that.stylesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, description, isActive, avatarCharactorId, booksId, stylesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AvatarAttributesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (avatarCharactorId != null ? "avatarCharactorId=" + avatarCharactorId + ", " : "") +
            (booksId != null ? "booksId=" + booksId + ", " : "") +
            (stylesId != null ? "stylesId=" + stylesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
