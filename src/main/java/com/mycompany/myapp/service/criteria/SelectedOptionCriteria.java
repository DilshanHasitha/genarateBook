package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.SelectedOption} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.SelectedOptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /selected-options?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SelectedOptionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private LocalDateFilter date;

    private LongFilter booksId;

    private LongFilter customerId;

    private LongFilter selectedOptionDetailsId;

    private Boolean distinct;

    public SelectedOptionCriteria() {}

    public SelectedOptionCriteria(SelectedOptionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.booksId = other.booksId == null ? null : other.booksId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.selectedOptionDetailsId = other.selectedOptionDetailsId == null ? null : other.selectedOptionDetailsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SelectedOptionCriteria copy() {
        return new SelectedOptionCriteria(this);
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

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
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

    public LongFilter getCustomerId() {
        return customerId;
    }

    public LongFilter customerId() {
        if (customerId == null) {
            customerId = new LongFilter();
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getSelectedOptionDetailsId() {
        return selectedOptionDetailsId;
    }

    public LongFilter selectedOptionDetailsId() {
        if (selectedOptionDetailsId == null) {
            selectedOptionDetailsId = new LongFilter();
        }
        return selectedOptionDetailsId;
    }

    public void setSelectedOptionDetailsId(LongFilter selectedOptionDetailsId) {
        this.selectedOptionDetailsId = selectedOptionDetailsId;
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
        final SelectedOptionCriteria that = (SelectedOptionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(date, that.date) &&
            Objects.equals(booksId, that.booksId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(selectedOptionDetailsId, that.selectedOptionDetailsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, date, booksId, customerId, selectedOptionDetailsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SelectedOptionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (booksId != null ? "booksId=" + booksId + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (selectedOptionDetailsId != null ? "selectedOptionDetailsId=" + selectedOptionDetailsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
