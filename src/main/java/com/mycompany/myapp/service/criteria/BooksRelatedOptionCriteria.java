package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.BooksRelatedOption} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.BooksRelatedOptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /books-related-options?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BooksRelatedOptionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private BooleanFilter isActive;

    private LongFilter booksRelatedOptionDetailsId;

    private LongFilter booksId;

    private Boolean distinct;

    public BooksRelatedOptionCriteria() {}

    public BooksRelatedOptionCriteria(BooksRelatedOptionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.booksRelatedOptionDetailsId = other.booksRelatedOptionDetailsId == null ? null : other.booksRelatedOptionDetailsId.copy();
        this.booksId = other.booksId == null ? null : other.booksId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BooksRelatedOptionCriteria copy() {
        return new BooksRelatedOptionCriteria(this);
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

    public LongFilter getBooksRelatedOptionDetailsId() {
        return booksRelatedOptionDetailsId;
    }

    public LongFilter booksRelatedOptionDetailsId() {
        if (booksRelatedOptionDetailsId == null) {
            booksRelatedOptionDetailsId = new LongFilter();
        }
        return booksRelatedOptionDetailsId;
    }

    public void setBooksRelatedOptionDetailsId(LongFilter booksRelatedOptionDetailsId) {
        this.booksRelatedOptionDetailsId = booksRelatedOptionDetailsId;
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
        final BooksRelatedOptionCriteria that = (BooksRelatedOptionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(booksRelatedOptionDetailsId, that.booksRelatedOptionDetailsId) &&
            Objects.equals(booksId, that.booksId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, isActive, booksRelatedOptionDetailsId, booksId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BooksRelatedOptionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (booksRelatedOptionDetailsId != null ? "booksRelatedOptionDetailsId=" + booksRelatedOptionDetailsId + ", " : "") +
            (booksId != null ? "booksId=" + booksId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
