package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Styles.
 */
@Entity
@Table(name = "styles")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Styles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "img_url")
    private String imgURL;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "x")
    private Integer x;

    @Column(name = "y")
    private Integer y;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Styles id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Styles code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public Styles description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgURL() {
        return this.imgURL;
    }

    public Styles imgURL(String imgURL) {
        this.setImgURL(imgURL);
        return this;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Styles isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getWidth() {
        return this.width;
    }

    public Styles width(Integer width) {
        this.setWidth(width);
        return this;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return this.height;
    }

    public Styles height(Integer height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getX() {
        return this.x;
    }

    public Styles x(Integer x) {
        this.setX(x);
        return this;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return this.y;
    }

    public Styles y(Integer y) {
        this.setY(y);
        return this;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Styles)) {
            return false;
        }
        return id != null && id.equals(((Styles) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Styles{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", imgURL='" + getImgURL() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", width=" + getWidth() +
            ", height=" + getHeight() +
            ", x=" + getX() +
            ", y=" + getY() +
            "}";
    }
}
