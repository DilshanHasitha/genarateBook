package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.BooksPage} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.BooksPageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /books-pages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BooksPageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter num;

    private BooleanFilter isActive;

    private LongFilter pageDetailsId;

    private LongFilter booksId;

    private Boolean distinct;

    public BooksPageCriteria() {}

    public BooksPageCriteria(BooksPageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.num = other.num == null ? null : other.num.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.pageDetailsId = other.pageDetailsId == null ? null : other.pageDetailsId.copy();
        this.booksId = other.booksId == null ? null : other.booksId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BooksPageCriteria copy() {
        return new BooksPageCriteria(this);
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

    public IntegerFilter getNum() {
        return num;
    }

    public IntegerFilter num() {
        if (num == null) {
            num = new IntegerFilter();
        }
        return num;
    }

    public void setNum(IntegerFilter num) {
        this.num = num;
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

    public LongFilter getPageDetailsId() {
        return pageDetailsId;
    }

    public LongFilter pageDetailsId() {
        if (pageDetailsId == null) {
            pageDetailsId = new LongFilter();
        }
        return pageDetailsId;
    }

    public void setPageDetailsId(LongFilter pageDetailsId) {
        this.pageDetailsId = pageDetailsId;
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
        final BooksPageCriteria that = (BooksPageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(num, that.num) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(pageDetailsId, that.pageDetailsId) &&
            Objects.equals(booksId, that.booksId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, num, isActive, pageDetailsId, booksId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BooksPageCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (num != null ? "num=" + num + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (pageDetailsId != null ? "pageDetailsId=" + pageDetailsId + ", " : "") +
            (booksId != null ? "booksId=" + booksId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
