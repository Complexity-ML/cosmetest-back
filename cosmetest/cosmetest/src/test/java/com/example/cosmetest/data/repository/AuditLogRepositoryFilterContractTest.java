package com.example.cosmetest.data.repository;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AuditLogRepositoryFilterContractTest {

    @Test
    void searchCombinesActionUserEntityAndDateFilters() throws Exception {
        Query query = AuditLogRepository.class
                .getMethod("search", String.class, String.class,
                        com.example.cosmetest.domain.model.AuditLog.Action.class,
                        LocalDateTime.class, LocalDateTime.class,
                        org.springframework.data.domain.Pageable.class)
                .getAnnotation(Query.class);

        assertThat(query).isNotNull();
        assertThat(query.value().toLowerCase())
                .contains(":action is null or a.action = :action")
                .contains(":utilisateur is null")
                .contains(":entite is null")
                .contains(":debut is null")
                .contains(":fin is null")
                .contains("order by a.createdat desc");
    }
}
