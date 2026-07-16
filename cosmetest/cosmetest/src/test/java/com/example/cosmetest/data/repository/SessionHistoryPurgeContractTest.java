package com.example.cosmetest.data.repository;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.Modifying;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class SessionHistoryPurgeContractTest {

    @Test
    void purgeTargetsOnlySessionsEndedBeforeCutoff() throws Exception {
        var method = SessionHistoryRepository.class
                .getMethod("deleteByLogoutTimeBefore", Instant.class);

        assertThat(method.getAnnotation(Modifying.class)).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(int.class);
    }
}
