package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BooksRelatedOptionDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BooksRelatedOptionDetails.class);
        BooksRelatedOptionDetails booksRelatedOptionDetails1 = new BooksRelatedOptionDetails();
        booksRelatedOptionDetails1.setId(1L);
        BooksRelatedOptionDetails booksRelatedOptionDetails2 = new BooksRelatedOptionDetails();
        booksRelatedOptionDetails2.setId(booksRelatedOptionDetails1.getId());
        assertThat(booksRelatedOptionDetails1).isEqualTo(booksRelatedOptionDetails2);
        booksRelatedOptionDetails2.setId(2L);
        assertThat(booksRelatedOptionDetails1).isNotEqualTo(booksRelatedOptionDetails2);
        booksRelatedOptionDetails1.setId(null);
        assertThat(booksRelatedOptionDetails1).isNotEqualTo(booksRelatedOptionDetails2);
    }
}
