package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ImageStoreType.
 */
@Entity
@Table(name = "image_store_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ImageStoreType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "image_store_type_code", nullable = false)
    private String imageStoreTypeCode;

    @NotNull
    @Column(name = "image_store_type_description", nullable = false)
    private String imageStoreTypeDescription;

    @Column(name = "is_active")
    private Boolean isActive;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageStoreTypeCode() {
        return imageStoreTypeCode;
    }

    public ImageStoreType imageStoreTypeCode(String imageStoreTypeCode) {
        this.imageStoreTypeCode = imageStoreTypeCode;
        return this;
    }

    public void setImageStoreTypeCode(String imageStoreTypeCode) {
        this.imageStoreTypeCode = imageStoreTypeCode;
    }

    public String getImageStoreTypeDescription() {
        return imageStoreTypeDescription;
    }

    public ImageStoreType imageStoreTypeDescription(String imageStoreTypeDescription) {
        this.imageStoreTypeDescription = imageStoreTypeDescription;
        return this;
    }

    public void setImageStoreTypeDescription(String imageStoreTypeDescription) {
        this.imageStoreTypeDescription = imageStoreTypeDescription;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public ImageStoreType isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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
        return 31;
    }

    @Override
    public String toString() {
        return (
            "ImageStoreType{" +
            "id=" +
            getId() +
            ", imageStoreTypeCode='" +
            getImageStoreTypeCode() +
            "'" +
            ", imageStoreTypeDescription='" +
            getImageStoreTypeDescription() +
            "'" +
            ", isActive='" +
            isIsActive() +
            "'" +
            "}"
        );
    }
}
