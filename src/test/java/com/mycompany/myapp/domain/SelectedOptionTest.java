package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SelectedOptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SelectedOption.class);
        SelectedOption selectedOption1 = new SelectedOption();
        selectedOption1.setId(1L);
        SelectedOption selectedOption2 = new SelectedOption();
        selectedOption2.setId(selectedOption1.getId());
        assertThat(selectedOption1).isEqualTo(selectedOption2);
        selectedOption2.setId(2L);
        assertThat(selectedOption1).isNotEqualTo(selectedOption2);
        selectedOption1.setId(null);
        assertThat(selectedOption1).isNotEqualTo(selectedOption2);
    }
}
