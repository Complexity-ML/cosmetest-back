package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.domain.model.VolontaireHc;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VolontaireHcFieldRegistryTest {

    @Test
    void exposesExplicitSafeAccessorsAndSqlColumnNames() {
        VolontaireHc entity = new VolontaireHc();
        var field = VolontaireHcFieldRegistry.require("antiTranspirant");

        field.set(entity, "occasionnellement");

        assertThat(field.get(entity)).isEqualTo("occasionnellement");
        assertThat(field.columnName()).isEqualTo("anti_transpirant");
    }

    @Test
    void neverExposesTheTechnicalIdentifierAsADynamicProduct() {
        assertThatThrownBy(() -> VolontaireHcFieldRegistry.require("idVol"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
