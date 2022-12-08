package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OptionTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OptionType.class);
        OptionType optionType1 = new OptionType();
        optionType1.setId(1L);
        OptionType optionType2 = new OptionType();
        optionType2.setId(optionType1.getId());
        assertThat(optionType1).isEqualTo(optionType2);
        optionType2.setId(2L);
        assertThat(optionType1).isNotEqualTo(optionType2);
        optionType1.setId(null);
        assertThat(optionType1).isNotEqualTo(optionType2);
    }
}
