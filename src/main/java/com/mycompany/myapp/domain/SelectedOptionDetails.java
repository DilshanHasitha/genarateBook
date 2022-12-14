package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SelectedOptionDetails.
 */
@Entity
@Table(name = "selected_option_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SelectedOptionDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "selected_value")
    private String selectedValue;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "selected_style_code")
    private String selectedStyleCode;

    @Column(name = "selected_option_code")
    private String selectedOptionCode;

    @ManyToMany(mappedBy = "selectedOptionDetails")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "books", "customer", "selectedOptionDetails" }, allowSetters = true)
    private Set<SelectedOption> selectedOptions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SelectedOptionDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public SelectedOptionDetails code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public SelectedOptionDetails name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelectedValue() {
        return this.selectedValue;
    }

    public SelectedOptionDetails selectedValue(String selectedValue) {
        this.setSelectedValue(selectedValue);
        return this;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public SelectedOptionDetails isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getSelectedStyleCode() {
        return this.selectedStyleCode;
    }

    public SelectedOptionDetails selectedStyleCode(String selectedStyleCode) {
        this.setSelectedStyleCode(selectedStyleCode);
        return this;
    }

    public void setSelectedStyleCode(String selectedStyleCode) {
        this.selectedStyleCode = selectedStyleCode;
    }

    public String getSelectedOptionCode() {
        return this.selectedOptionCode;
    }

    public SelectedOptionDetails selectedOptionCode(String selectedOptionCode) {
        this.setSelectedOptionCode(selectedOptionCode);
        return this;
    }

    public void setSelectedOptionCode(String selectedOptionCode) {
        this.selectedOptionCode = selectedOptionCode;
    }

    public Set<SelectedOption> getSelectedOptions() {
        return this.selectedOptions;
    }

    public void setSelectedOptions(Set<SelectedOption> selectedOptions) {
        if (this.selectedOptions != null) {
            this.selectedOptions.forEach(i -> i.removeSelectedOptionDetails(this));
        }
        if (selectedOptions != null) {
            selectedOptions.forEach(i -> i.addSelectedOptionDetails(this));
        }
        this.selectedOptions = selectedOptions;
    }

    public SelectedOptionDetails selectedOptions(Set<SelectedOption> selectedOptions) {
        this.setSelectedOptions(selectedOptions);
        return this;
    }

    public SelectedOptionDetails addSelectedOption(SelectedOption selectedOption) {
        this.selectedOptions.add(selectedOption);
        selectedOption.getSelectedOptionDetails().add(this);
        return this;
    }

    public SelectedOptionDetails removeSelectedOption(SelectedOption selectedOption) {
        this.selectedOptions.remove(selectedOption);
        selectedOption.getSelectedOptionDetails().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SelectedOptionDetails)) {
            return false;
        }
        return id != null && id.equals(((SelectedOptionDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SelectedOptionDetails{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", selectedValue='" + getSelectedValue() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", selectedStyleCode='" + getSelectedStyleCode() + "'" +
            ", selectedOptionCode='" + getSelectedOptionCode() + "'" +
            "}";
    }
}
