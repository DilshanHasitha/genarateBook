package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FontFamilyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FontFamily.class);
        FontFamily fontFamily1 = new FontFamily();
        fontFamily1.setId(1L);
        FontFamily fontFamily2 = new FontFamily();
        fontFamily2.setId(fontFamily1.getId());
        assertThat(fontFamily1).isEqualTo(fontFamily2);
        fontFamily2.setId(2L);
        assertThat(fontFamily1).isNotEqualTo(fontFamily2);
        fontFamily1.setId(null);
        assertThat(fontFamily1).isNotEqualTo(fontFamily2);
    }
}
