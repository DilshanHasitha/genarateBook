package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.BooksPage;
import java.time.LocalDate;
import java.util.Set;

public class BooksPageDTO {

    private String code;
    private Set<BooksPage> booksPages;

    public BooksPageDTO() {}

    public BooksPageDTO(String code, Set<BooksPage> booksPages) {
        this.code = code;
        this.booksPages = booksPages;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<BooksPage> getBooksPages() {
        return booksPages;
    }

    public void setBooksPages(Set<BooksPage> booksPages) {
        this.booksPages = booksPages;
    }
}
