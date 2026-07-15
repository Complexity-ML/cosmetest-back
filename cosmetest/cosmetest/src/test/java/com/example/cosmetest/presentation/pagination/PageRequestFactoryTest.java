package com.example.cosmetest.presentation.pagination;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PageRequestFactoryTest {

    @Test
    void normalizesPageAndBoundsSize() {
        var pageable = PageRequestFactory.create(-4, 10_000, "dateDebut", "DESC", Set.of("dateDebut"));

        assertThat(pageable.getPageNumber()).isZero();
        assertThat(pageable.getPageSize()).isEqualTo(100);
        assertThat(pageable.getSort().getOrderFor("dateDebut").getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void rejectsUnknownSortProperties() {
        assertThatThrownBy(() -> PageRequestFactory.create(0, 20, "sqlInjection", "ASC", Set.of("dateDebut")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsUnknownSortDirections() {
        assertThatThrownBy(() -> PageRequestFactory.create(0, 20, "dateDebut", "SIDEWAYS", Set.of("dateDebut")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
