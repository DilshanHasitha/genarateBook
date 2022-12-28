package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Images.
 */
@Entity
@Table(name = "images")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Images implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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
    private ImageStoreType storeType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Images id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImageBlob() {
        return this.imageBlob;
    }

    public Images imageBlob(byte[] imageBlob) {
        this.setImageBlob(imageBlob);
        return this;
    }

    public void setImageBlob(byte[] imageBlob) {
        this.imageBlob = imageBlob;
    }

    public String getImageBlobContentType() {
        return this.imageBlobContentType;
    }

    public Images imageBlobContentType(String imageBlobContentType) {
        this.imageBlobContentType = imageBlobContentType;
        return this;
    }

    public void setImageBlobContentType(String imageBlobContentType) {
        this.imageBlobContentType = imageBlobContentType;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public Images imageURL(String imageURL) {
        this.setImageURL(imageURL);
        return this;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageName() {
        return this.imageName;
    }

    public Images imageName(String imageName) {
        this.setImageName(imageName);
        return this;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getLowResURL() {
        return this.lowResURL;
    }

    public Images lowResURL(String lowResURL) {
        this.setLowResURL(lowResURL);
        return this;
    }

    public void setLowResURL(String lowResURL) {
        this.lowResURL = lowResURL;
    }

    public String getOriginalURL() {
        return this.originalURL;
    }

    public Images originalURL(String originalURL) {
        this.setOriginalURL(originalURL);
        return this;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public ImageStoreType getStoreType() {
        return this.storeType;
    }

    public void setStoreType(ImageStoreType imageStoreType) {
        this.storeType = imageStoreType;
    }

    public Images storeType(ImageStoreType imageStoreType) {
        this.setStoreType(imageStoreType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Images{" +
            "id=" + getId() +
            ", imageBlob='" + getImageBlob() + "'" +
            ", imageBlobContentType='" + getImageBlobContentType() + "'" +
            ", imageURL='" + getImageURL() + "'" +
            ", imageName='" + getImageName() + "'" +
            ", lowResURL='" + getLowResURL() + "'" +
            ", originalURL='" + getOriginalURL() + "'" +
            "}";
    }
}
