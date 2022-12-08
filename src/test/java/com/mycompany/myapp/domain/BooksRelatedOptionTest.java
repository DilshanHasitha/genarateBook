package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BooksRelatedOptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BooksRelatedOption.class);
        BooksRelatedOption booksRelatedOption1 = new BooksRelatedOption();
        booksRelatedOption1.setId(1L);
        BooksRelatedOption booksRelatedOption2 = new BooksRelatedOption();
        booksRelatedOption2.setId(booksRelatedOption1.getId());
        assertThat(booksRelatedOption1).isEqualTo(booksRelatedOption2);
        booksRelatedOption2.setId(2L);
        assertThat(booksRelatedOption1).isNotEqualTo(booksRelatedOption2);
        booksRelatedOption1.setId(null);
        assertThat(booksRelatedOption1).isNotEqualTo(booksRelatedOption2);
    }
}
