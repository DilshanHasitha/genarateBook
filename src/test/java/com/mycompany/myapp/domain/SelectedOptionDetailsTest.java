package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SelectedOptionDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SelectedOptionDetails.class);
        SelectedOptionDetails selectedOptionDetails1 = new SelectedOptionDetails();
        selectedOptionDetails1.setId(1L);
        SelectedOptionDetails selectedOptionDetails2 = new SelectedOptionDetails();
        selectedOptionDetails2.setId(selectedOptionDetails1.getId());
        assertThat(selectedOptionDetails1).isEqualTo(selectedOptionDetails2);
        selectedOptionDetails2.setId(2L);
        assertThat(selectedOptionDetails1).isNotEqualTo(selectedOptionDetails2);
        selectedOptionDetails1.setId(null);
        assertThat(selectedOptionDetails1).isNotEqualTo(selectedOptionDetails2);
    }
}
