package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.DocCheckFieldDTO;
import com.example.cosmetest.business.dto.DocCheckImportPreviewDTO;
import com.example.cosmetest.business.dto.DocCheckImportRequestDTO;
import com.example.cosmetest.business.dto.VolontaireDTO;
import com.example.cosmetest.business.dto.VolontaireDetailDTO;
import com.example.cosmetest.business.service.VolontaireService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires - DocCheckImportServiceImpl")
class DocCheckImportServiceImplTest {

    @Mock
    private VolontaireService volontaireService;

    @InjectMocks
    private DocCheckImportServiceImpl service;

    @Test
    @DisplayName("preview() mappe fields[] vers VolontaireDetailDTO")
    void previewMapsFieldsToVolontaireDetail() {
        DocCheckImportRequestDTO request = request(
                field("nom", "Dupont", 0.96),
                field("prenom", "Marie", 0.95),
                field("telephone", "6 12 34 56 78", 0.94),
                field("dateNaissance", "18/04/1992", 0.93),
                field("email", "marie@example.fr", 0.92),
                field("contraception", "Pilule", 0.91));

        when(volontaireService.findByEmail("marie@example.fr")).thenReturn(Optional.empty());
        when(volontaireService.searchByMultipleFields(
                eq("Dupont"),
                eq("Marie"),
                any(),
                eq("0612345678"),
                any(),
                any(),
                any(),
                anyBoolean(),
                anyInt(),
                anyInt())).thenReturn(new PageImpl<>(List.of()));

        DocCheckImportPreviewDTO preview = service.preview(request);

        assertThat(preview.isCanImport()).isTrue();
        assertThat(preview.getWarnings()).isEmpty();
        assertThat(preview.getDuplicateCandidates()).isEmpty();
        VolontaireDetailDTO volontaire = preview.getVolontaire();
        assertThat(volontaire.getNomVol()).isEqualTo("Dupont");
        assertThat(volontaire.getPrenomVol()).isEqualTo("Marie");
        assertThat(volontaire.getTelPortableVol()).isEqualTo("0612345678");
        assertThat(volontaire.getDateNaissance()).isEqualTo(LocalDate.of(1992, 4, 18));
        assertThat(volontaire.getEmailVol()).isEqualTo("marie@example.fr");
        assertThat(volontaire.getContraception()).isEqualTo("Pilule");
    }

    @Test
    @DisplayName("confirm() bloque si un doublon est detecte")
    void confirmBlocksDuplicateCandidate() {
        DocCheckImportRequestDTO request = request(
                field("nom", "Dupont", 0.96),
                field("prenom", "Marie", 0.95),
                field("telephone", "0612345678", 0.94),
                field("dateNaissance", "1992-04-18", 0.93),
                field("email", "marie@example.fr", 0.92));

        VolontaireDTO existing = new VolontaireDTO();
        existing.setIdVol(12);
        existing.setNomVol("Dupont");
        existing.setPrenomVol("Marie");
        existing.setEmailVol("marie@example.fr");

        when(volontaireService.findByEmail("marie@example.fr")).thenReturn(Optional.of(existing));
        when(volontaireService.searchByMultipleFields(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                anyBoolean(),
                anyInt(),
                anyInt())).thenReturn(new PageImpl<>(List.of()));

        assertThatThrownBy(() -> service.confirm(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Import bloque");

        verify(volontaireService, never()).createVolontaireDetail(any());
    }

    private DocCheckImportRequestDTO request(DocCheckFieldDTO... fields) {
        DocCheckImportRequestDTO request = new DocCheckImportRequestDTO();
        request.setDocumentId("doc-1");
        request.setFields(List.of(fields));
        return request;
    }

    private DocCheckFieldDTO field(String key, String value, double confidence) {
        DocCheckFieldDTO field = new DocCheckFieldDTO();
        field.setKey(key);
        field.setLabel(key);
        field.setValue(value);
        field.setConfidence(confidence);
        return field;
    }
}
