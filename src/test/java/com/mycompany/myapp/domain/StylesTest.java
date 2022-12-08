package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StylesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Styles.class);
        Styles styles1 = new Styles();
        styles1.setId(1L);
        Styles styles2 = new Styles();
        styles2.setId(styles1.getId());
        assertThat(styles1).isEqualTo(styles2);
        styles2.setId(2L);
        assertThat(styles1).isNotEqualTo(styles2);
        styles1.setId(null);
        assertThat(styles1).isNotEqualTo(styles2);
    }
}
