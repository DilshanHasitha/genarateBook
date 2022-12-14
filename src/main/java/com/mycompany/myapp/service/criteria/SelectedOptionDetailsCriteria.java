package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.SelectedOptionDetails} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.SelectedOptionDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /selected-option-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SelectedOptionDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private StringFilter selectedValue;

    private BooleanFilter isActive;

    private StringFilter selectedStyleCode;

    private StringFilter selectedOptionCode;

    private LongFilter selectedOptionId;

    private Boolean distinct;

    public SelectedOptionDetailsCriteria() {}

    public SelectedOptionDetailsCriteria(SelectedOptionDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.selectedValue = other.selectedValue == null ? null : other.selectedValue.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.selectedStyleCode = other.selectedStyleCode == null ? null : other.selectedStyleCode.copy();
        this.selectedOptionCode = other.selectedOptionCode == null ? null : other.selectedOptionCode.copy();
        this.selectedOptionId = other.selectedOptionId == null ? null : other.selectedOptionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SelectedOptionDetailsCriteria copy() {
        return new SelectedOptionDetailsCriteria(this);
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

    public StringFilter getSelectedValue() {
        return selectedValue;
    }

    public StringFilter selectedValue() {
        if (selectedValue == null) {
            selectedValue = new StringFilter();
        }
        return selectedValue;
    }

    public void setSelectedValue(StringFilter selectedValue) {
        this.selectedValue = selectedValue;
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

    public StringFilter getSelectedStyleCode() {
        return selectedStyleCode;
    }

    public StringFilter selectedStyleCode() {
        if (selectedStyleCode == null) {
            selectedStyleCode = new StringFilter();
        }
        return selectedStyleCode;
    }

    public void setSelectedStyleCode(StringFilter selectedStyleCode) {
        this.selectedStyleCode = selectedStyleCode;
    }

    public StringFilter getSelectedOptionCode() {
        return selectedOptionCode;
    }

    public StringFilter selectedOptionCode() {
        if (selectedOptionCode == null) {
            selectedOptionCode = new StringFilter();
        }
        return selectedOptionCode;
    }

    public void setSelectedOptionCode(StringFilter selectedOptionCode) {
        this.selectedOptionCode = selectedOptionCode;
    }

    public LongFilter getSelectedOptionId() {
        return selectedOptionId;
    }

    public LongFilter selectedOptionId() {
        if (selectedOptionId == null) {
            selectedOptionId = new LongFilter();
        }
        return selectedOptionId;
    }

    public void setSelectedOptionId(LongFilter selectedOptionId) {
        this.selectedOptionId = selectedOptionId;
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
        final SelectedOptionDetailsCriteria that = (SelectedOptionDetailsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(selectedValue, that.selectedValue) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(selectedStyleCode, that.selectedStyleCode) &&
            Objects.equals(selectedOptionCode, that.selectedOptionCode) &&
            Objects.equals(selectedOptionId, that.selectedOptionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, selectedValue, isActive, selectedStyleCode, selectedOptionCode, selectedOptionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SelectedOptionDetailsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (selectedValue != null ? "selectedValue=" + selectedValue + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (selectedStyleCode != null ? "selectedStyleCode=" + selectedStyleCode + ", " : "") +
            (selectedOptionCode != null ? "selectedOptionCode=" + selectedOptionCode + ", " : "") +
            (selectedOptionId != null ? "selectedOptionId=" + selectedOptionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
