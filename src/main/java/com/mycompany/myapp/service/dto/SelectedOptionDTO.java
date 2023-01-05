package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.Books;
import com.mycompany.myapp.domain.SelectedOptionDetails;
import java.util.Set;

public class SelectedOptionDTO {

    private String code;
    private String bookCode;
    private Set<SelectedOptionDetails> selectedOptionDetails;

    public SelectedOptionDTO() {}

    public SelectedOptionDTO(String code, String bookCode, Set<SelectedOptionDetails> selectedOptionDetails) {
        this.code = code;
        this.bookCode = bookCode;
        this.selectedOptionDetails = selectedOptionDetails;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public Set<SelectedOptionDetails> getSelectedOptionDetails() {
        return selectedOptionDetails;
    }

    public void setSelectedOptionDetails(Set<SelectedOptionDetails> selectedOptionDetails) {
        this.selectedOptionDetails = selectedOptionDetails;
    }
}
