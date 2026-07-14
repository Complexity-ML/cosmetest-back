package com.example.cosmetest.domain.model;

import com.example.cosmetest.data.repository.InfobancaireRepository;
import com.example.cosmetest.data.repository.PreetudevolRepository;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.junit.jupiter.api.Test;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class SurrogateIdMigrationTest {

    @Test
    void infobancaireUsesGeneratedLongIdAndLongRepository() throws Exception {
        assertGeneratedLongId(Infobancaire.class, "idInfobancaire", "ID_INFOBANCAIRE");
        assertThat(new DefaultRepositoryMetadata(InfobancaireRepository.class).getIdType()).isEqualTo(Long.class);
    }

    @Test
    void preetudevolUsesGeneratedLongIdAndLongRepository() throws Exception {
        assertGeneratedLongId(Preetudevol.class, "idPreetudevol", "ID_PREETUDEVOL");
        assertThat(new DefaultRepositoryMetadata(PreetudevolRepository.class).getIdType()).isEqualTo(Long.class);
    }

    private void assertGeneratedLongId(Class<?> entityType, String fieldName, String columnName) throws Exception {
        assertThat(entityType.getDeclaredFields())
                .noneMatch(field -> field.isAnnotationPresent(EmbeddedId.class));
        Field id = entityType.getDeclaredField(fieldName);
        assertThat(id.getType()).isEqualTo(Long.class);
        assertThat(id.isAnnotationPresent(Id.class)).isTrue();
        assertThat(id.isAnnotationPresent(GeneratedValue.class)).isTrue();
        assertThat(id.getAnnotation(Column.class).name()).isEqualTo(columnName);
    }
}
