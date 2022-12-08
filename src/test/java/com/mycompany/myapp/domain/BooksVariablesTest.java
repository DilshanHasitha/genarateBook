package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BooksVariablesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BooksVariables.class);
        BooksVariables booksVariables1 = new BooksVariables();
        booksVariables1.setId(1L);
        BooksVariables booksVariables2 = new BooksVariables();
        booksVariables2.setId(booksVariables1.getId());
        assertThat(booksVariables1).isEqualTo(booksVariables2);
        booksVariables2.setId(2L);
        assertThat(booksVariables1).isNotEqualTo(booksVariables2);
        booksVariables1.setId(null);
        assertThat(booksVariables1).isNotEqualTo(booksVariables2);
    }
}
