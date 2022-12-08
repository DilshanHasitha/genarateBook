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
 * A Options.
 */
@Entity
@Table(name = "options")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Options implements Serializable {

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

    @ManyToMany
    @JoinTable(
        name = "rel_options__style",
        joinColumns = @JoinColumn(name = "options_id"),
        inverseJoinColumns = @JoinColumn(name = "style_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "options" }, allowSetters = true)
    private Set<Styles> styles = new HashSet<>();

    @ManyToMany(mappedBy = "options")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "avatarCharactors", "options", "books" }, allowSetters = true)
    private Set<AvatarAttributes> avatarAttributes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Options id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Options code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public Options description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgURL() {
        return this.imgURL;
    }

    public Options imgURL(String imgURL) {
        this.setImgURL(imgURL);
        return this;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Options isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Styles> getStyles() {
        return this.styles;
    }

    public void setStyles(Set<Styles> styles) {
        this.styles = styles;
    }

    public Options styles(Set<Styles> styles) {
        this.setStyles(styles);
        return this;
    }

    public Options addStyle(Styles styles) {
        this.styles.add(styles);
        styles.getOptions().add(this);
        return this;
    }

    public Options removeStyle(Styles styles) {
        this.styles.remove(styles);
        styles.getOptions().remove(this);
        return this;
    }

    public Set<AvatarAttributes> getAvatarAttributes() {
        return this.avatarAttributes;
    }

    public void setAvatarAttributes(Set<AvatarAttributes> avatarAttributes) {
        if (this.avatarAttributes != null) {
            this.avatarAttributes.forEach(i -> i.removeOption(this));
        }
        if (avatarAttributes != null) {
            avatarAttributes.forEach(i -> i.addOption(this));
        }
        this.avatarAttributes = avatarAttributes;
    }

    public Options avatarAttributes(Set<AvatarAttributes> avatarAttributes) {
        this.setAvatarAttributes(avatarAttributes);
        return this;
    }

    public Options addAvatarAttributes(AvatarAttributes avatarAttributes) {
        this.avatarAttributes.add(avatarAttributes);
        avatarAttributes.getOptions().add(this);
        return this;
    }

    public Options removeAvatarAttributes(AvatarAttributes avatarAttributes) {
        this.avatarAttributes.remove(avatarAttributes);
        avatarAttributes.getOptions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Options)) {
            return false;
        }
        return id != null && id.equals(((Options) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Options{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", imgURL='" + getImgURL() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
