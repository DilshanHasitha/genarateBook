package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Books} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.BooksResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /books?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BooksCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private StringFilter title;

    private StringFilter subTitle;

    private StringFilter author;

    private BooleanFilter isActive;

    private IntegerFilter noOfPages;

    private StringFilter storeImg;

    private LongFilter pageSizeId;

    private LongFilter userId;

    private LongFilter booksPageId;

    private LongFilter priceRelatedOptionId;

    private LongFilter booksRelatedOptionId;

    private LongFilter booksAttributesId;

    private LongFilter booksVariablesId;

    private LongFilter avatarAttributesId;

    private LongFilter layerGroupId;

    private LongFilter selectionsId;

    private Boolean distinct;

    public BooksCriteria() {}

    public BooksCriteria(BooksCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.subTitle = other.subTitle == null ? null : other.subTitle.copy();
        this.author = other.author == null ? null : other.author.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.noOfPages = other.noOfPages == null ? null : other.noOfPages.copy();
        this.storeImg = other.storeImg == null ? null : other.storeImg.copy();
        this.pageSizeId = other.pageSizeId == null ? null : other.pageSizeId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.booksPageId = other.booksPageId == null ? null : other.booksPageId.copy();
        this.priceRelatedOptionId = other.priceRelatedOptionId == null ? null : other.priceRelatedOptionId.copy();
        this.booksRelatedOptionId = other.booksRelatedOptionId == null ? null : other.booksRelatedOptionId.copy();
        this.booksAttributesId = other.booksAttributesId == null ? null : other.booksAttributesId.copy();
        this.booksVariablesId = other.booksVariablesId == null ? null : other.booksVariablesId.copy();
        this.avatarAttributesId = other.avatarAttributesId == null ? null : other.avatarAttributesId.copy();
        this.layerGroupId = other.layerGroupId == null ? null : other.layerGroupId.copy();
        this.selectionsId = other.selectionsId == null ? null : other.selectionsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BooksCriteria copy() {
        return new BooksCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getSubTitle() {
        return subTitle;
    }

    public StringFilter subTitle() {
        if (subTitle == null) {
            subTitle = new StringFilter();
        }
        return subTitle;
    }

    public void setSubTitle(StringFilter subTitle) {
        this.subTitle = subTitle;
    }

    public StringFilter getAuthor() {
        return author;
    }

    public StringFilter author() {
        if (author == null) {
            author = new StringFilter();
        }
        return author;
    }

    public void setAuthor(StringFilter author) {
        this.author = author;
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

    public IntegerFilter getNoOfPages() {
        return noOfPages;
    }

    public IntegerFilter noOfPages() {
        if (noOfPages == null) {
            noOfPages = new IntegerFilter();
        }
        return noOfPages;
    }

    public void setNoOfPages(IntegerFilter noOfPages) {
        this.noOfPages = noOfPages;
    }

    public StringFilter getStoreImg() {
        return storeImg;
    }

    public StringFilter storeImg() {
        if (storeImg == null) {
            storeImg = new StringFilter();
        }
        return storeImg;
    }

    public void setStoreImg(StringFilter storeImg) {
        this.storeImg = storeImg;
    }

    public LongFilter getPageSizeId() {
        return pageSizeId;
    }

    public LongFilter pageSizeId() {
        if (pageSizeId == null) {
            pageSizeId = new LongFilter();
        }
        return pageSizeId;
    }

    public void setPageSizeId(LongFilter pageSizeId) {
        this.pageSizeId = pageSizeId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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

    public LongFilter getPriceRelatedOptionId() {
        return priceRelatedOptionId;
    }

    public LongFilter priceRelatedOptionId() {
        if (priceRelatedOptionId == null) {
            priceRelatedOptionId = new LongFilter();
        }
        return priceRelatedOptionId;
    }

    public void setPriceRelatedOptionId(LongFilter priceRelatedOptionId) {
        this.priceRelatedOptionId = priceRelatedOptionId;
    }

    public LongFilter getBooksRelatedOptionId() {
        return booksRelatedOptionId;
    }

    public LongFilter booksRelatedOptionId() {
        if (booksRelatedOptionId == null) {
            booksRelatedOptionId = new LongFilter();
        }
        return booksRelatedOptionId;
    }

    public void setBooksRelatedOptionId(LongFilter booksRelatedOptionId) {
        this.booksRelatedOptionId = booksRelatedOptionId;
    }

    public LongFilter getBooksAttributesId() {
        return booksAttributesId;
    }

    public LongFilter booksAttributesId() {
        if (booksAttributesId == null) {
            booksAttributesId = new LongFilter();
        }
        return booksAttributesId;
    }

    public void setBooksAttributesId(LongFilter booksAttributesId) {
        this.booksAttributesId = booksAttributesId;
    }

    public LongFilter getBooksVariablesId() {
        return booksVariablesId;
    }

    public LongFilter booksVariablesId() {
        if (booksVariablesId == null) {
            booksVariablesId = new LongFilter();
        }
        return booksVariablesId;
    }

    public void setBooksVariablesId(LongFilter booksVariablesId) {
        this.booksVariablesId = booksVariablesId;
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

    public LongFilter getSelectionsId() {
        return selectionsId;
    }

    public LongFilter selectionsId() {
        if (selectionsId == null) {
            selectionsId = new LongFilter();
        }
        return selectionsId;
    }

    public void setSelectionsId(LongFilter selectionsId) {
        this.selectionsId = selectionsId;
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
        final BooksCriteria that = (BooksCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(title, that.title) &&
            Objects.equals(subTitle, that.subTitle) &&
            Objects.equals(author, that.author) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(noOfPages, that.noOfPages) &&
            Objects.equals(storeImg, that.storeImg) &&
            Objects.equals(pageSizeId, that.pageSizeId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(booksPageId, that.booksPageId) &&
            Objects.equals(priceRelatedOptionId, that.priceRelatedOptionId) &&
            Objects.equals(booksRelatedOptionId, that.booksRelatedOptionId) &&
            Objects.equals(booksAttributesId, that.booksAttributesId) &&
            Objects.equals(booksVariablesId, that.booksVariablesId) &&
            Objects.equals(avatarAttributesId, that.avatarAttributesId) &&
            Objects.equals(layerGroupId, that.layerGroupId) &&
            Objects.equals(selectionsId, that.selectionsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            code,
            name,
            title,
            subTitle,
            author,
            isActive,
            noOfPages,
            storeImg,
            pageSizeId,
            userId,
            booksPageId,
            priceRelatedOptionId,
            booksRelatedOptionId,
            booksAttributesId,
            booksVariablesId,
            avatarAttributesId,
            layerGroupId,
            selectionsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BooksCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (subTitle != null ? "subTitle=" + subTitle + ", " : "") +
            (author != null ? "author=" + author + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (noOfPages != null ? "noOfPages=" + noOfPages + ", " : "") +
            (storeImg != null ? "storeImg=" + storeImg + ", " : "") +
            (pageSizeId != null ? "pageSizeId=" + pageSizeId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (booksPageId != null ? "booksPageId=" + booksPageId + ", " : "") +
            (priceRelatedOptionId != null ? "priceRelatedOptionId=" + priceRelatedOptionId + ", " : "") +
            (booksRelatedOptionId != null ? "booksRelatedOptionId=" + booksRelatedOptionId + ", " : "") +
            (booksAttributesId != null ? "booksAttributesId=" + booksAttributesId + ", " : "") +
            (booksVariablesId != null ? "booksVariablesId=" + booksVariablesId + ", " : "") +
            (avatarAttributesId != null ? "avatarAttributesId=" + avatarAttributesId + ", " : "") +
            (layerGroupId != null ? "layerGroupId=" + layerGroupId + ", " : "") +
            (selectionsId != null ? "selectionsId=" + selectionsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
