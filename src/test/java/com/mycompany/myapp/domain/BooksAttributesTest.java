package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BooksAttributesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BooksAttributes.class);
        BooksAttributes booksAttributes1 = new BooksAttributes();
        booksAttributes1.setId(1L);
        BooksAttributes booksAttributes2 = new BooksAttributes();
        booksAttributes2.setId(booksAttributes1.getId());
        assertThat(booksAttributes1).isEqualTo(booksAttributes2);
        booksAttributes2.setId(2L);
        assertThat(booksAttributes1).isNotEqualTo(booksAttributes2);
        booksAttributes1.setId(null);
        assertThat(booksAttributes1).isNotEqualTo(booksAttributes2);
    }
}
