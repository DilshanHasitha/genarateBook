package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Layers} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.LayersResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /layers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LayersCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter layerNo;

    private BooleanFilter isActive;

    private LongFilter layerdetailsId;

    private LongFilter layerGroupId;

    private Boolean distinct;

    public LayersCriteria() {}

    public LayersCriteria(LayersCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.layerNo = other.layerNo == null ? null : other.layerNo.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.layerdetailsId = other.layerdetailsId == null ? null : other.layerdetailsId.copy();
        this.layerGroupId = other.layerGroupId == null ? null : other.layerGroupId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LayersCriteria copy() {
        return new LayersCriteria(this);
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

    public LongFilter getLayerdetailsId() {
        return layerdetailsId;
    }

    public LongFilter layerdetailsId() {
        if (layerdetailsId == null) {
            layerdetailsId = new LongFilter();
        }
        return layerdetailsId;
    }

    public void setLayerdetailsId(LongFilter layerdetailsId) {
        this.layerdetailsId = layerdetailsId;
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
        final LayersCriteria that = (LayersCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(layerNo, that.layerNo) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(layerdetailsId, that.layerdetailsId) &&
            Objects.equals(layerGroupId, that.layerGroupId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, layerNo, isActive, layerdetailsId, layerGroupId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LayersCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (layerNo != null ? "layerNo=" + layerNo + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (layerdetailsId != null ? "layerdetailsId=" + layerdetailsId + ", " : "") +
            (layerGroupId != null ? "layerGroupId=" + layerGroupId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
