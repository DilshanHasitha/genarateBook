package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Selections.
 */
@Entity
@Table(name = "selections")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Selections implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "avatar_code")
    private String avatarCode;

    @Column(name = "style_code")
    private String styleCode;

    @Column(name = "option_code")
    private String optionCode;

    @Column(name = "image")
    private String image;

    @Column(name = "height")
    private Integer height;

    @Column(name = "x")
    private Integer x;

    @Column(name = "y")
    private Integer y;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "width")
    private Integer width;

    @Column(name = "avatar_attributes_code")
    private String avatarAttributesCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Selections id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatarCode() {
        return this.avatarCode;
    }

    public Selections avatarCode(String avatarCode) {
        this.setAvatarCode(avatarCode);
        return this;
    }

    public void setAvatarCode(String avatarCode) {
        this.avatarCode = avatarCode;
    }

    public String getStyleCode() {
        return this.styleCode;
    }

    public Selections styleCode(String styleCode) {
        this.setStyleCode(styleCode);
        return this;
    }

    public void setStyleCode(String styleCode) {
        this.styleCode = styleCode;
    }

    public String getOptionCode() {
        return this.optionCode;
    }

    public Selections optionCode(String optionCode) {
        this.setOptionCode(optionCode);
        return this;
    }

    public void setOptionCode(String optionCode) {
        this.optionCode = optionCode;
    }

    public String getImage() {
        return this.image;
    }

    public Selections image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getHeight() {
        return this.height;
    }

    public Selections height(Integer height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getX() {
        return this.x;
    }

    public Selections x(Integer x) {
        this.setX(x);
        return this;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return this.y;
    }

    public Selections y(Integer y) {
        this.setY(y);
        return this;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Selections isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getWidth() {
        return this.width;
    }

    public Selections width(Integer width) {
        this.setWidth(width);
        return this;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getAvatarAttributesCode() {
        return this.avatarAttributesCode;
    }

    public Selections avatarAttributesCode(String avatarAttributesCode) {
        this.setAvatarAttributesCode(avatarAttributesCode);
        return this;
    }

    public void setAvatarAttributesCode(String avatarAttributesCode) {
        this.avatarAttributesCode = avatarAttributesCode;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Selections)) {
            return false;
        }
        return id != null && id.equals(((Selections) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Selections{" +
            "id=" + getId() +
            ", avatarCode='" + getAvatarCode() + "'" +
            ", styleCode='" + getStyleCode() + "'" +
            ", optionCode='" + getOptionCode() + "'" +
            ", image='" + getImage() + "'" +
            ", height=" + getHeight() +
            ", x=" + getX() +
            ", y=" + getY() +
            ", isActive='" + getIsActive() + "'" +
            ", width=" + getWidth() +
            ", avatarAttributesCode='" + getAvatarAttributesCode() + "'" +
            "}";
    }
}
