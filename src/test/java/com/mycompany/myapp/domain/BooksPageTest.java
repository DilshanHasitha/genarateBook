package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BooksPageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BooksPage.class);
        BooksPage booksPage1 = new BooksPage();
        booksPage1.setId(1L);
        BooksPage booksPage2 = new BooksPage();
        booksPage2.setId(booksPage1.getId());
        assertThat(booksPage1).isEqualTo(booksPage2);
        booksPage2.setId(2L);
        assertThat(booksPage1).isNotEqualTo(booksPage2);
        booksPage1.setId(null);
        assertThat(booksPage1).isNotEqualTo(booksPage2);
    }
}
