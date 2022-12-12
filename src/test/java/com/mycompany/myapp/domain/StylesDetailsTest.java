package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StylesDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StylesDetails.class);
        StylesDetails stylesDetails1 = new StylesDetails();
        stylesDetails1.setId(1L);
        StylesDetails stylesDetails2 = new StylesDetails();
        stylesDetails2.setId(stylesDetails1.getId());
        assertThat(stylesDetails1).isEqualTo(stylesDetails2);
        stylesDetails2.setId(2L);
        assertThat(stylesDetails1).isNotEqualTo(stylesDetails2);
        stylesDetails1.setId(null);
        assertThat(stylesDetails1).isNotEqualTo(stylesDetails2);
    }
}
