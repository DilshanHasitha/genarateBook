package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
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

    @ManyToMany(mappedBy = "styles")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "styles", "avatarAttributes" }, allowSetters = true)
    private Set<Options> options = new HashSet<>();

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

    public Set<Options> getOptions() {
        return this.options;
    }

    public void setOptions(Set<Options> options) {
        if (this.options != null) {
            this.options.forEach(i -> i.removeStyle(this));
        }
        if (options != null) {
            options.forEach(i -> i.addStyle(this));
        }
        this.options = options;
    }

    public Styles options(Set<Options> options) {
        this.setOptions(options);
        return this;
    }

    public Styles addOption(Options options) {
        this.options.add(options);
        options.getStyles().add(this);
        return this;
    }

    public Styles removeOption(Options options) {
        this.options.remove(options);
        options.getStyles().remove(this);
        return this;
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
            "}";
    }
}
