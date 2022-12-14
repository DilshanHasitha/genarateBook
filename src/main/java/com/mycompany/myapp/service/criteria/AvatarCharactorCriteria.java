package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.AvatarCharactor} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.AvatarCharactorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /avatar-charactors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AvatarCharactorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter description;

    private BooleanFilter isActive;

    private StringFilter imgUrl;

    private IntegerFilter width;

    private IntegerFilter height;

    private IntegerFilter x;

    private IntegerFilter y;

    private LongFilter avatarAttributesId;

    private LongFilter layerGroupId;

    private LongFilter characterId;

    private Boolean distinct;

    public AvatarCharactorCriteria() {}

    public AvatarCharactorCriteria(AvatarCharactorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.imgUrl = other.imgUrl == null ? null : other.imgUrl.copy();
        this.width = other.width == null ? null : other.width.copy();
        this.height = other.height == null ? null : other.height.copy();
        this.x = other.x == null ? null : other.x.copy();
        this.y = other.y == null ? null : other.y.copy();
        this.avatarAttributesId = other.avatarAttributesId == null ? null : other.avatarAttributesId.copy();
        this.layerGroupId = other.layerGroupId == null ? null : other.layerGroupId.copy();
        this.characterId = other.characterId == null ? null : other.characterId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AvatarCharactorCriteria copy() {
        return new AvatarCharactorCriteria(this);
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

    public StringFilter getImgUrl() {
        return imgUrl;
    }

    public StringFilter imgUrl() {
        if (imgUrl == null) {
            imgUrl = new StringFilter();
        }
        return imgUrl;
    }

    public void setImgUrl(StringFilter imgUrl) {
        this.imgUrl = imgUrl;
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

    public LongFilter getAvatarAttributesId() {
        return avatarAttributesId;
    }

    public LongFilter avatarAttributesId() {
        if (avatarAttributesId == null) {
            avatarAttributesId = new LongFilter();
        }
        return avatarAttributesId;
    }

    public void setAvatarAttributesId(LongFilter avatarAttributesId) {
        this.avatarAttributesId = avatarAttributesId;
    }

    public LongFilter getLayerGroupId() {
        return layerGroupId;
    }

    public LongFilter layerGroupId() {
        if (layerGroupId == null) {
            layerGroupId = new LongFilter();
        }
        return layerGroupId;
    }

    public void setLayerGroupId(LongFilter layerGroupId) {
        this.layerGroupId = layerGroupId;
    }

    public LongFilter getCharacterId() {
        return characterId;
    }

    public LongFilter characterId() {
        if (characterId == null) {
            characterId = new LongFilter();
        }
        return characterId;
    }

    public void setCharacterId(LongFilter characterId) {
        this.characterId = characterId;
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
        final AvatarCharactorCriteria that = (AvatarCharactorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(description, that.description) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(imgUrl, that.imgUrl) &&
            Objects.equals(width, that.width) &&
            Objects.equals(height, that.height) &&
            Objects.equals(x, that.x) &&
            Objects.equals(y, that.y) &&
            Objects.equals(avatarAttributesId, that.avatarAttributesId) &&
            Objects.equals(layerGroupId, that.layerGroupId) &&
            Objects.equals(characterId, that.characterId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            code,
            description,
            isActive,
            imgUrl,
            width,
            height,
            x,
            y,
            avatarAttributesId,
            layerGroupId,
            characterId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AvatarCharactorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (imgUrl != null ? "imgUrl=" + imgUrl + ", " : "") +
            (width != null ? "width=" + width + ", " : "") +
            (height != null ? "height=" + height + ", " : "") +
            (x != null ? "x=" + x + ", " : "") +
            (y != null ? "y=" + y + ", " : "") +
            (avatarAttributesId != null ? "avatarAttributesId=" + avatarAttributesId + ", " : "") +
            (layerGroupId != null ? "layerGroupId=" + layerGroupId + ", " : "") +
            (characterId != null ? "characterId=" + characterId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
