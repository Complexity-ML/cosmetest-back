package com.example.cosmetest.domain.model;

import com.example.cosmetest.business.dto.RdvDTO;
import com.example.cosmetest.business.mapper.RdvMapper;
import com.example.cosmetest.data.repository.RdvRepository;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import static org.assertj.core.api.Assertions.assertThat;

class RdvTechnicalIdMappingTest {

    @Test
    void mapsRdvPkAsGeneratedJpaIdentifierAndKeepsBusinessIdentifiersAsColumns() throws Exception {
        Field technicalId = Rdv.class.getDeclaredField("rdvPk");
        Field studyId = Rdv.class.getDeclaredField("idEtude");
        Field businessNumber = Rdv.class.getDeclaredField("idRdv");

        assertThat(technicalId.getAnnotation(Id.class)).isNotNull();
        assertThat(technicalId.getAnnotation(Column.class).name()).isEqualTo("RDV_PK");
        assertThat(technicalId.getAnnotation(GeneratedValue.class).strategy()).isEqualTo(GenerationType.IDENTITY);
        assertThat(studyId.getAnnotation(Column.class).name()).isEqualTo("ID_ETUDE");
        assertThat(businessNumber.getAnnotation(Column.class).name()).isEqualTo("ID_RDV");

        ParameterizedType repositoryType = (ParameterizedType) RdvRepository.class.getGenericInterfaces()[0];
        assertThat(repositoryType.getActualTypeArguments()[1]).isEqualTo(Long.class);
    }

    @Test
    void mapperExposesTechnicalAndBusinessIdentifiers() throws Exception {
        Rdv rdv = new Rdv();
        setField(rdv, "rdvPk", 9001L);
        setField(rdv, "idEtude", 42);
        setField(rdv, "idRdv", 7);

        RdvDTO dto = new RdvMapper().toDto(rdv);

        assertThat(readField(dto, "rdvPk")).isEqualTo(9001L);
        assertThat(dto.getIdEtude()).isEqualTo(42);
        assertThat(dto.getIdRdv()).isEqualTo(7);
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private static Object readField(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }
}
