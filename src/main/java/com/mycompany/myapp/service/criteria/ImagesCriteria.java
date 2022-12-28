package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Images} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ImagesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /images?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImagesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter imageURL;

    private StringFilter imageName;

    private StringFilter lowResURL;

    private StringFilter originalURL;

    private LongFilter storeTypeId;

    private Boolean distinct;

    public ImagesCriteria() {}

    public ImagesCriteria(ImagesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.imageURL = other.imageURL == null ? null : other.imageURL.copy();
        this.imageName = other.imageName == null ? null : other.imageName.copy();
        this.lowResURL = other.lowResURL == null ? null : other.lowResURL.copy();
        this.originalURL = other.originalURL == null ? null : other.originalURL.copy();
        this.storeTypeId = other.storeTypeId == null ? null : other.storeTypeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ImagesCriteria copy() {
        return new ImagesCriteria(this);
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

    public StringFilter getImageURL() {
        return imageURL;
    }

    public StringFilter imageURL() {
        if (imageURL == null) {
            imageURL = new StringFilter();
        }
        return imageURL;
    }

    public void setImageURL(StringFilter imageURL) {
        this.imageURL = imageURL;
    }

    public StringFilter getImageName() {
        return imageName;
    }

    public StringFilter imageName() {
        if (imageName == null) {
            imageName = new StringFilter();
        }
        return imageName;
    }

    public void setImageName(StringFilter imageName) {
        this.imageName = imageName;
    }

    public StringFilter getLowResURL() {
        return lowResURL;
    }

    public StringFilter lowResURL() {
        if (lowResURL == null) {
            lowResURL = new StringFilter();
        }
        return lowResURL;
    }

    public void setLowResURL(StringFilter lowResURL) {
        this.lowResURL = lowResURL;
    }

    public StringFilter getOriginalURL() {
        return originalURL;
    }

    public StringFilter originalURL() {
        if (originalURL == null) {
            originalURL = new StringFilter();
        }
        return originalURL;
    }

    public void setOriginalURL(StringFilter originalURL) {
        this.originalURL = originalURL;
    }

    public LongFilter getStoreTypeId() {
        return storeTypeId;
    }

    public LongFilter storeTypeId() {
        if (storeTypeId == null) {
            storeTypeId = new LongFilter();
        }
        return storeTypeId;
    }

    public void setStoreTypeId(LongFilter storeTypeId) {
        this.storeTypeId = storeTypeId;
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
        final ImagesCriteria that = (ImagesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(imageURL, that.imageURL) &&
            Objects.equals(imageName, that.imageName) &&
            Objects.equals(lowResURL, that.lowResURL) &&
            Objects.equals(originalURL, that.originalURL) &&
            Objects.equals(storeTypeId, that.storeTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imageURL, imageName, lowResURL, originalURL, storeTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImagesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (imageURL != null ? "imageURL=" + imageURL + ", " : "") +
            (imageName != null ? "imageName=" + imageName + ", " : "") +
            (lowResURL != null ? "lowResURL=" + lowResURL + ", " : "") +
            (originalURL != null ? "originalURL=" + originalURL + ", " : "") +
            (storeTypeId != null ? "storeTypeId=" + storeTypeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
