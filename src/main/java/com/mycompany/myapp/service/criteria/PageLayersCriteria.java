package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.PageLayers} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.PageLayersResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /page-layers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PageLayersCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter layerNo;

    private BooleanFilter isActive;

    private BooleanFilter isEditable;

    private BooleanFilter isText;

    private LongFilter pageElementDetailsId;

    private LongFilter booksPageId;

    private Boolean distinct;

    public PageLayersCriteria() {}

    public PageLayersCriteria(PageLayersCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.layerNo = other.layerNo == null ? null : other.layerNo.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.isEditable = other.isEditable == null ? null : other.isEditable.copy();
        this.isText = other.isText == null ? null : other.isText.copy();
        this.pageElementDetailsId = other.pageElementDetailsId == null ? null : other.pageElementDetailsId.copy();
        this.booksPageId = other.booksPageId == null ? null : other.booksPageId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PageLayersCriteria copy() {
        return new PageLayersCriteria(this);
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

    public IntegerFilter getLayerNo() {
        return layerNo;
    }

    public IntegerFilter layerNo() {
        if (layerNo == null) {
            layerNo = new IntegerFilter();
        }
        return layerNo;
    }

    public void setLayerNo(IntegerFilter layerNo) {
        this.layerNo = layerNo;
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

    public BooleanFilter getIsEditable() {
        return isEditable;
    }

    public BooleanFilter isEditable() {
        if (isEditable == null) {
            isEditable = new BooleanFilter();
        }
        return isEditable;
    }

    public void setIsEditable(BooleanFilter isEditable) {
        this.isEditable = isEditable;
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

    public LongFilter getPageElementDetailsId() {
        return pageElementDetailsId;
    }

    public LongFilter pageElementDetailsId() {
        if (pageElementDetailsId == null) {
            pageElementDetailsId = new LongFilter();
        }
        return pageElementDetailsId;
    }

    public void setPageElementDetailsId(LongFilter pageElementDetailsId) {
        this.pageElementDetailsId = pageElementDetailsId;
    }

    public LongFilter getBooksPageId() {
        return booksPageId;
    }

    public LongFilter booksPageId() {
        if (booksPageId == null) {
            booksPageId = new LongFilter();
        }
        return booksPageId;
    }

    public void setBooksPageId(LongFilter booksPageId) {
        this.booksPageId = booksPageId;
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
        final PageLayersCriteria that = (PageLayersCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(layerNo, that.layerNo) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(isEditable, that.isEditable) &&
            Objects.equals(isText, that.isText) &&
            Objects.equals(pageElementDetailsId, that.pageElementDetailsId) &&
            Objects.equals(booksPageId, that.booksPageId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, layerNo, isActive, isEditable, isText, pageElementDetailsId, booksPageId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PageLayersCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (layerNo != null ? "layerNo=" + layerNo + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (isEditable != null ? "isEditable=" + isEditable + ", " : "") +
            (isText != null ? "isText=" + isText + ", " : "") +
            (pageElementDetailsId != null ? "pageElementDetailsId=" + pageElementDetailsId + ", " : "") +
            (booksPageId != null ? "booksPageId=" + booksPageId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
