package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PriceRelatedOptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PriceRelatedOption.class);
        PriceRelatedOption priceRelatedOption1 = new PriceRelatedOption();
        priceRelatedOption1.setId(1L);
        PriceRelatedOption priceRelatedOption2 = new PriceRelatedOption();
        priceRelatedOption2.setId(priceRelatedOption1.getId());
        assertThat(priceRelatedOption1).isEqualTo(priceRelatedOption2);
        priceRelatedOption2.setId(2L);
        assertThat(priceRelatedOption1).isNotEqualTo(priceRelatedOption2);
        priceRelatedOption1.setId(null);
        assertThat(priceRelatedOption1).isNotEqualTo(priceRelatedOption2);
    }
}
