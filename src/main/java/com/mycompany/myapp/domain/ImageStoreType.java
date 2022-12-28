package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ImageStoreType.
 */
@Entity
@Table(name = "image_store_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageStoreType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "image_store_type_code", nullable = false)
    private String imageStoreTypeCode;

    @NotNull
    @Column(name = "image_store_type_description", nullable = false)
    private String imageStoreTypeDescription;

    @Column(name = "is_active")
    private Boolean isActive;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ImageStoreType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageStoreTypeCode() {
        return this.imageStoreTypeCode;
    }

    public ImageStoreType imageStoreTypeCode(String imageStoreTypeCode) {
        this.setImageStoreTypeCode(imageStoreTypeCode);
        return this;
    }

    public void setImageStoreTypeCode(String imageStoreTypeCode) {
        this.imageStoreTypeCode = imageStoreTypeCode;
    }

    public String getImageStoreTypeDescription() {
        return this.imageStoreTypeDescription;
    }

    public ImageStoreType imageStoreTypeDescription(String imageStoreTypeDescription) {
        this.setImageStoreTypeDescription(imageStoreTypeDescription);
        return this;
    }

    public void setImageStoreTypeDescription(String imageStoreTypeDescription) {
        this.imageStoreTypeDescription = imageStoreTypeDescription;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public ImageStoreType isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageStoreType)) {
            return false;
        }
        return id != null && id.equals(((ImageStoreType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageStoreType{" +
            "id=" + getId() +
            ", imageStoreTypeCode='" + getImageStoreTypeCode() + "'" +
            ", imageStoreTypeDescription='" + getImageStoreTypeDescription() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
