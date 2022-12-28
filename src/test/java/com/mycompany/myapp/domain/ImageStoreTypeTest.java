package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageStoreTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageStoreType.class);
        ImageStoreType imageStoreType1 = new ImageStoreType();
        imageStoreType1.setId(1L);
        ImageStoreType imageStoreType2 = new ImageStoreType();
        imageStoreType2.setId(imageStoreType1.getId());
        assertThat(imageStoreType1).isEqualTo(imageStoreType2);
        imageStoreType2.setId(2L);
        assertThat(imageStoreType1).isNotEqualTo(imageStoreType2);
        imageStoreType1.setId(null);
        assertThat(imageStoreType1).isNotEqualTo(imageStoreType2);
    }
}
