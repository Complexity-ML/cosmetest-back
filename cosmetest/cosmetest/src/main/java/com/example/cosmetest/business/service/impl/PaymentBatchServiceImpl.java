package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.AnnulationDTO;
import com.example.cosmetest.business.dto.EtudeVolontaireDTO;
import com.example.cosmetest.business.dto.PaymentBatchResultDTO;
import com.example.cosmetest.business.service.AnnulationService;
import com.example.cosmetest.business.service.EtudeService;
import com.example.cosmetest.business.service.EtudeVolontaireService;
import com.example.cosmetest.business.service.PaymentBatchService;
import com.example.cosmetest.exception.AmbiguousEtudeVolontaireException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class PaymentBatchServiceImpl implements PaymentBatchService {
    private final EtudeVolontaireService associations;
    private final AnnulationService annulations;
    private final EtudeService etudes;
    public PaymentBatchServiceImpl(EtudeVolontaireService a, AnnulationService n, EtudeService e){associations=a;annulations=n;etudes=e;}

    @Override @Transactional
    public PaymentBatchResultDTO markAllAsPaid(int idEtude) {
        PaymentBatchResultDTO result=new PaymentBatchResultDTO(); result.setIdEtude(idEtude);
        List<EtudeVolontaireDTO> rows=associations.getEtudeVolontairesByEtude(idEtude);
        result.setProcessedCount(rows==null?0:rows.size());
        if(rows==null||rows.isEmpty()){updateStudy(idEtude,0);return result;}

        Map<Integer,Integer> counts=new HashMap<>();
        rows.forEach(row->counts.merge(row.getIdVolontaire(),1,Integer::sum));
        List<Integer> duplicates=counts.entrySet().stream().filter(e->e.getValue()>1)
                .map(Map.Entry::getKey).sorted().toList();
        if(!duplicates.isEmpty()) throw new AmbiguousEtudeVolontaireException(
                "Paiement en lot refusé: associations dupliquées pour les volontaires "+duplicates);

        Set<Integer> cancelled=new HashSet<>();
        List<AnnulationDTO> cancellationRows=annulations.getAnnulationsByEtude(idEtude);
        if(cancellationRows!=null) cancellationRows.forEach(a->cancelled.add(a.getIdVol()));
        int updated=0, skipped=0, paid=0;
        for(EtudeVolontaireDTO row:rows){
            if(row.getPaye()==1){paid++;continue;}
            if(cancelled.contains(row.getIdVolontaire())){skipped++;continue;}
            if(row.getId()==null) throw new IllegalStateException("ID technique manquant pour le volontaire "+row.getIdVolontaire());
            associations.updatePaye(row.getId(),1); updated++;
        }
        result.setUpdatedCount(updated); result.setSkippedAnnules(skipped); result.setAlreadyPaidCount(paid);
        updateStudy(idEtude,rows.size()-skipped>0?2:0);
        return result;
    }
    private void updateStudy(int id,int paye){if(!etudes.updatePayeStatus(id,paye))throw new IllegalStateException("Échec de mise à jour du statut PAYE de l'étude "+id+" à "+paye);}
}
