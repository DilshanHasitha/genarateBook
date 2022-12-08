package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LayerGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LayerGroup.class);
        LayerGroup layerGroup1 = new LayerGroup();
        layerGroup1.setId(1L);
        LayerGroup layerGroup2 = new LayerGroup();
        layerGroup2.setId(layerGroup1.getId());
        assertThat(layerGroup1).isEqualTo(layerGroup2);
        layerGroup2.setId(2L);
        assertThat(layerGroup1).isNotEqualTo(layerGroup2);
        layerGroup1.setId(null);
        assertThat(layerGroup1).isNotEqualTo(layerGroup2);
    }
}
