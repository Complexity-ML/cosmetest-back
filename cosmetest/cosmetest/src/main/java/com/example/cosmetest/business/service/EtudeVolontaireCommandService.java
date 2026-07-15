package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.EtudeVolontaireDTO;
import com.example.cosmetest.domain.model.AuditLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EtudeVolontaireCommandService {

    private final EtudeVolontaireService etudeVolontaireService;
    private final AuditLogService auditLogService;

    public EtudeVolontaireCommandService(EtudeVolontaireService etudeVolontaireService,
                                         AuditLogService auditLogService) {
        this.etudeVolontaireService = etudeVolontaireService;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public EtudeVolontaireDTO create(EtudeVolontaireDTO request,
                                     String utilisateur,
                                     String adresseIp) {
        EtudeVolontaireDTO created = etudeVolontaireService.saveEtudeVolontaire(request);
        String entiteId = created.getIdEtude() + "-" + created.getIdGroupe() + "-" + created.getIdVolontaire();
        String details = "etude=" + created.getIdEtude()
                + ", groupe=" + created.getIdGroupe()
                + ", volontaire=" + created.getIdVolontaire();
        auditLogService.log(utilisateur, AuditLog.Action.ASSIGN, "ETUDE_VOLONTAIRE",
                entiteId, details, adresseIp);
        return created;
    }
}
