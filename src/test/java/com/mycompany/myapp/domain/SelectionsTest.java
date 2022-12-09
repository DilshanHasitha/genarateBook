package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SelectionsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Selections.class);
        Selections selections1 = new Selections();
        selections1.setId(1L);
        Selections selections2 = new Selections();
        selections2.setId(selections1.getId());
        assertThat(selections1).isEqualTo(selections2);
        selections2.setId(2L);
        assertThat(selections1).isNotEqualTo(selections2);
        selections1.setId(null);
        assertThat(selections1).isNotEqualTo(selections2);
    }
}
