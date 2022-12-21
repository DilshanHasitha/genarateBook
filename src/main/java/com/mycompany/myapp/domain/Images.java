package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Images.
 */
@Entity
@Table(name = "images")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Images implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "image_blob")
    private byte[] imageBlob;

    @Column(name = "image_blob_content_type")
    private String imageBlobContentType;

    @Column(name = "image_url")
    private String imageURL;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "low_res_url")
    private String lowResURL;

    @Column(name = "original_url")
    private String originalURL;

    @ManyToOne
    @JsonIgnoreProperties("images")
    private ImageStoreType storeType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImageBlob() {
        return imageBlob;
    }

    public Images imageBlob(byte[] imageBlob) {
        this.imageBlob = imageBlob;
        return this;
    }

    public void setImageBlob(byte[] imageBlob) {
        this.imageBlob = imageBlob;
    }

    public String getImageBlobContentType() {
        return imageBlobContentType;
    }

    public Images imageBlobContentType(String imageBlobContentType) {
        this.imageBlobContentType = imageBlobContentType;
        return this;
    }

    public void setImageBlobContentType(String imageBlobContentType) {
        this.imageBlobContentType = imageBlobContentType;
    }

    public String getImageURL() {
        return imageURL;
    }

    public Images imageURL(String imageURL) {
        this.imageURL = imageURL;
        return this;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageName() {
        return imageName;
    }

    public Images imageName(String imageName) {
        this.imageName = imageName;
        return this;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getLowResURL() {
        return lowResURL;
    }

    public Images lowResURL(String lowResURL) {
        this.lowResURL = lowResURL;
        return this;
    }

    public void setLowResURL(String lowResURL) {
        this.lowResURL = lowResURL;
    }

    public String getOriginalURL() {
        return originalURL;
    }

    public Images originalURL(String originalURL) {
        this.originalURL = originalURL;
        return this;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public ImageStoreType getStoreType() {
        return storeType;
    }

    public Images storeType(ImageStoreType imageStoreType) {
        this.storeType = imageStoreType;
        return this;
    }

    public void setStoreType(ImageStoreType imageStoreType) {
        this.storeType = imageStoreType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Images)) {
            return false;
        }
        return id != null && id.equals(((Images) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Images{" +
            "id=" +
            getId() +
            ", imageBlob='" +
            getImageBlob() +
            "'" +
            ", imageBlobContentType='" +
            getImageBlobContentType() +
            "'" +
            ", imageURL='" +
            getImageURL() +
            "'" +
            ", imageName='" +
            getImageName() +
            "'" +
            ", lowResURL='" +
            getLowResURL() +
            "'" +
            ", originalURL='" +
            getOriginalURL() +
            "'" +
            "}"
        );
    }
}
