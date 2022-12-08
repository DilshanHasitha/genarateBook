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
 * A AvatarCharactor.
 */
@Entity
@Table(name = "avatar_charactor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AvatarCharactor implements Serializable {

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

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToMany(mappedBy = "avatarCharactors")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "avatarCharactors", "options", "books" }, allowSetters = true)
    private Set<AvatarAttributes> avatarAttributes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AvatarCharactor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public AvatarCharactor code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public AvatarCharactor description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public AvatarCharactor isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<AvatarAttributes> getAvatarAttributes() {
        return this.avatarAttributes;
    }

    public void setAvatarAttributes(Set<AvatarAttributes> avatarAttributes) {
        if (this.avatarAttributes != null) {
            this.avatarAttributes.forEach(i -> i.removeAvatarCharactor(this));
        }
        if (avatarAttributes != null) {
            avatarAttributes.forEach(i -> i.addAvatarCharactor(this));
        }
        this.avatarAttributes = avatarAttributes;
    }

    public AvatarCharactor avatarAttributes(Set<AvatarAttributes> avatarAttributes) {
        this.setAvatarAttributes(avatarAttributes);
        return this;
    }

    public AvatarCharactor addAvatarAttributes(AvatarAttributes avatarAttributes) {
        this.avatarAttributes.add(avatarAttributes);
        avatarAttributes.getAvatarCharactors().add(this);
        return this;
    }

    public AvatarCharactor removeAvatarAttributes(AvatarAttributes avatarAttributes) {
        this.avatarAttributes.remove(avatarAttributes);
        avatarAttributes.getAvatarCharactors().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AvatarCharactor)) {
            return false;
        }
        return id != null && id.equals(((AvatarCharactor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AvatarCharactor{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
