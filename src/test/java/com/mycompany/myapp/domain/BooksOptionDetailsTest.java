package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BooksOptionDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BooksOptionDetails.class);
        BooksOptionDetails booksOptionDetails1 = new BooksOptionDetails();
        booksOptionDetails1.setId(1L);
        BooksOptionDetails booksOptionDetails2 = new BooksOptionDetails();
        booksOptionDetails2.setId(booksOptionDetails1.getId());
        assertThat(booksOptionDetails1).isEqualTo(booksOptionDetails2);
        booksOptionDetails2.setId(2L);
        assertThat(booksOptionDetails1).isNotEqualTo(booksOptionDetails2);
        booksOptionDetails1.setId(null);
        assertThat(booksOptionDetails1).isNotEqualTo(booksOptionDetails2);
    }
}
