package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.PaiementEtudeSummaryDTO;
import com.example.cosmetest.business.service.PaiementStatsService;
import com.example.cosmetest.data.repository.EtudeVolontaireRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaiementStatsServiceImpl implements PaiementStatsService {

    private final EtudeVolontaireRepository etudeVolontaireRepository;

    public PaiementStatsServiceImpl(EtudeVolontaireRepository etudeVolontaireRepository) {
        this.etudeVolontaireRepository = etudeVolontaireRepository;
    }

    @Override
    public List<PaiementEtudeSummaryDTO> getAllEtudeSummaries() {
        List<Object[]> rows = etudeVolontaireRepository.fetchEtudePaiementSummaries(null);
        if (rows == null || rows.isEmpty()) {
            return Collections.emptyList();
        }
        return rows.stream()
                .map(this::mapRow)
                .collect(Collectors.toList());
    }

    @Override
    public PaiementEtudeSummaryDTO getSummaryForEtude(int idEtude) {
        List<Object[]> rows = etudeVolontaireRepository.fetchEtudePaiementSummaries(idEtude);
        if (rows == null || rows.isEmpty()) {
            return null;
        }
        return mapRow(rows.get(0));
    }

    private PaiementEtudeSummaryDTO mapRow(Object[] row) {
        PaiementEtudeSummaryDTO dto = new PaiementEtudeSummaryDTO();
        dto.setIdEtude(getInt(row[0]));
        dto.setTotal(getLong(row[1]));
        dto.setPayes(getLong(row[2]));
        dto.setNonPayes(getLong(row[3]));
        dto.setEnAttente(getLong(row[4]));
        dto.setAnnules(getLong(row[5]));
        dto.setMontantTotal(getLong(row[6]));
        dto.setMontantPaye(getLong(row[7]));
        dto.setMontantAnnules(getLong(row[8]));
        dto.setMontantRestant(dto.getMontantTotal() - dto.getMontantPaye());
        return dto;
    }

    private long getLong(Object value) {
        return value == null ? 0L : ((Number) value).longValue();
    }

    private Integer getInt(Object value) {
        return value == null ? null : ((Number) value).intValue();
    }
}
