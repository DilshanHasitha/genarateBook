package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LayerDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LayerDetails.class);
        LayerDetails layerDetails1 = new LayerDetails();
        layerDetails1.setId(1L);
        LayerDetails layerDetails2 = new LayerDetails();
        layerDetails2.setId(layerDetails1.getId());
        assertThat(layerDetails1).isEqualTo(layerDetails2);
        layerDetails2.setId(2L);
        assertThat(layerDetails1).isNotEqualTo(layerDetails2);
        layerDetails1.setId(null);
        assertThat(layerDetails1).isNotEqualTo(layerDetails2);
    }
}
