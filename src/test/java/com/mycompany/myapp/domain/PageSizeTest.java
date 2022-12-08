package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PageSizeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PageSize.class);
        PageSize pageSize1 = new PageSize();
        pageSize1.setId(1L);
        PageSize pageSize2 = new PageSize();
        pageSize2.setId(pageSize1.getId());
        assertThat(pageSize1).isEqualTo(pageSize2);
        pageSize2.setId(2L);
        assertThat(pageSize1).isNotEqualTo(pageSize2);
        pageSize1.setId(null);
        assertThat(pageSize1).isNotEqualTo(pageSize2);
    }
}
