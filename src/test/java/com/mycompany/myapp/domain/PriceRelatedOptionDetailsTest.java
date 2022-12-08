package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PriceRelatedOptionDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PriceRelatedOptionDetails.class);
        PriceRelatedOptionDetails priceRelatedOptionDetails1 = new PriceRelatedOptionDetails();
        priceRelatedOptionDetails1.setId(1L);
        PriceRelatedOptionDetails priceRelatedOptionDetails2 = new PriceRelatedOptionDetails();
        priceRelatedOptionDetails2.setId(priceRelatedOptionDetails1.getId());
        assertThat(priceRelatedOptionDetails1).isEqualTo(priceRelatedOptionDetails2);
        priceRelatedOptionDetails2.setId(2L);
        assertThat(priceRelatedOptionDetails1).isNotEqualTo(priceRelatedOptionDetails2);
        priceRelatedOptionDetails1.setId(null);
        assertThat(priceRelatedOptionDetails1).isNotEqualTo(priceRelatedOptionDetails2);
    }
}
