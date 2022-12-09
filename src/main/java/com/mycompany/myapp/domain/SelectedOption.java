package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SelectedOption.
 */
@Entity
@Table(name = "selected_option")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SelectedOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "pageSize",
            "user",
            "booksPages",
            "priceRelatedOptions",
            "booksRelatedOptions",
            "booksAttributes",
            "booksVariables",
            "avatarAttributes",
            "layerGroups",
            "selections",
        },
        allowSetters = true
    )
    private Books books;

    @ManyToOne
    private Customer customer;

    @ManyToMany
    @JoinTable(
        name = "rel_selected_option__selected_option_details",
        joinColumns = @JoinColumn(name = "selected_option_id"),
        inverseJoinColumns = @JoinColumn(name = "selected_option_details_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "selectedOptions" }, allowSetters = true)
    private Set<SelectedOptionDetails> selectedOptionDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SelectedOption id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public SelectedOption code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public SelectedOption date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Books getBooks() {
        return this.books;
    }

    public void setBooks(Books books) {
        this.books = books;
    }

    public SelectedOption books(Books books) {
        this.setBooks(books);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public SelectedOption customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public Set<SelectedOptionDetails> getSelectedOptionDetails() {
        return this.selectedOptionDetails;
    }

    public void setSelectedOptionDetails(Set<SelectedOptionDetails> selectedOptionDetails) {
        this.selectedOptionDetails = selectedOptionDetails;
    }

    public SelectedOption selectedOptionDetails(Set<SelectedOptionDetails> selectedOptionDetails) {
        this.setSelectedOptionDetails(selectedOptionDetails);
        return this;
    }

    public SelectedOption addSelectedOptionDetails(SelectedOptionDetails selectedOptionDetails) {
        this.selectedOptionDetails.add(selectedOptionDetails);
        selectedOptionDetails.getSelectedOptions().add(this);
        return this;
    }

    public SelectedOption removeSelectedOptionDetails(SelectedOptionDetails selectedOptionDetails) {
        this.selectedOptionDetails.remove(selectedOptionDetails);
        selectedOptionDetails.getSelectedOptions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SelectedOption)) {
            return false;
        }
        return id != null && id.equals(((SelectedOption) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SelectedOption{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
