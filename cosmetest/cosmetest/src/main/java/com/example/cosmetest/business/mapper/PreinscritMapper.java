package com.example.cosmetest.business.mapper;

import com.example.cosmetest.business.dto.PreinscritDTO;
import com.example.cosmetest.domain.model.Preinscrit;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper pour convertir entre l'entité Preinscrit et PreinscritDTO
 */
@Component
public class PreinscritMapper {

    /**
     * Convertit une entité Preinscrit en PreinscritDTO
     *
     * @param preinscrit l'entité à convertir
     * @return le DTO correspondant
     */
    public PreinscritDTO toDTO(Preinscrit preinscrit) {
        if (preinscrit == null) {
            return null;
        }

        PreinscritDTO dto = new PreinscritDTO();
        dto.setIdPreinscrit(preinscrit.getIdPreinscrit());
        dto.setDatePreInscription(preinscrit.getDatePreInscription());
        dto.setTitre(preinscrit.getTitre());
        dto.setNom(preinscrit.getNom());
        dto.setPrenom(preinscrit.getPrenom());
        dto.setAdresse(preinscrit.getAdresse());
        dto.setCodePostal(preinscrit.getCodePostal());
        dto.setVille(preinscrit.getVille());
        dto.setTelDomicile(preinscrit.getTelDomicile());
        dto.setTelPortable(preinscrit.getTelPortable());
        dto.setEmail(preinscrit.getEmail());
        dto.setSexe(preinscrit.getSexe());
        dto.setDateNaissance(preinscrit.getDateNaissance());
        dto.setPhototype(preinscrit.getPhototype());
        dto.setPeauSensible(preinscrit.getPeauSensible());
        dto.setMapyeux(preinscrit.getMapyeux());
        dto.setMaplevres(preinscrit.getMaplevres());
        dto.setMapsourcils(preinscrit.getMapsourcils());
        dto.setPeauVisage(preinscrit.getPeauVisage());
        dto.setRideVisage(preinscrit.getRideVisage());
        dto.setTachePigmentaireVisage(preinscrit.getTachePigmentaireVisage());
        dto.setBoutonsVisage(preinscrit.getBoutonsVisage());
        dto.setComedonsVisage(preinscrit.getComedonsVisage());
        dto.setPoresDilatesVisage(preinscrit.getPoresDilatesVisage());
        dto.setPochesYeuxVisage(preinscrit.getPochesYeuxVisage());
        dto.setCernesVisage(preinscrit.getCernesVisage());
        dto.setEthnie(preinscrit.getEthnie());
        dto.setRdvDate(preinscrit.getRdvDate());
        dto.setRdvHeure(preinscrit.getRdvHeure());
        dto.setEtat(preinscrit.getEtat());
        dto.setCommentaires(preinscrit.getCommentaires());

        return dto;
    }

    /**
     * Convertit un PreinscritDTO en entité Preinscrit
     *
     * @param dto le DTO à convertir
     * @return l'entité correspondante
     */
    public Preinscrit toEntity(PreinscritDTO dto) {
        if (dto == null) {
            return null;
        }

        Preinscrit preinscrit = new Preinscrit();
        preinscrit.setIdPreinscrit(dto.getIdPreinscrit());
        preinscrit.setDatePreInscription(dto.getDatePreInscription());
        preinscrit.setTitre(dto.getTitre());
        preinscrit.setNom(dto.getNom());
        preinscrit.setPrenom(dto.getPrenom());
        preinscrit.setAdresse(dto.getAdresse());
        preinscrit.setCodePostal(dto.getCodePostal());
        preinscrit.setVille(dto.getVille());
        preinscrit.setTelDomicile(dto.getTelDomicile());
        preinscrit.setTelPortable(dto.getTelPortable());
        preinscrit.setEmail(dto.getEmail());
        preinscrit.setSexe(dto.getSexe());
        preinscrit.setDateNaissance(dto.getDateNaissance());
        preinscrit.setPhototype(dto.getPhototype());
        preinscrit.setPeauSensible(dto.getPeauSensible());
        preinscrit.setMapyeux(dto.getMapyeux());
        preinscrit.setMaplevres(dto.getMaplevres());
        preinscrit.setMapsourcils(dto.getMapsourcils());
        preinscrit.setPeauVisage(dto.getPeauVisage());
        preinscrit.setRideVisage(dto.getRideVisage());
        preinscrit.setTachePigmentaireVisage(dto.getTachePigmentaireVisage());
        preinscrit.setBoutonsVisage(dto.getBoutonsVisage());
        preinscrit.setComedonsVisage(dto.getComedonsVisage());
        preinscrit.setPoresDilatesVisage(dto.getPoresDilatesVisage());
        preinscrit.setPochesYeuxVisage(dto.getPochesYeuxVisage());
        preinscrit.setCernesVisage(dto.getCernesVisage());
        preinscrit.setEthnie(dto.getEthnie());
        preinscrit.setRdvDate(dto.getRdvDate());
        preinscrit.setRdvHeure(dto.getRdvHeure());
        preinscrit.setEtat(dto.getEtat());
        preinscrit.setCommentaires(dto.getCommentaires());

        return preinscrit;
    }

    /**
     * Convertit une liste d'entités Preinscrit en liste de PreinscritDTO
     *
     * @param preinscrits la liste d'entités
     * @return la liste de DTOs
     */
    public List<PreinscritDTO> toDTOList(List<Preinscrit> preinscrits) {
        return preinscrits.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de PreinscritDTO en liste d'entités Preinscrit
     *
     * @param dtos la liste de DTOs
     * @return la liste d'entités
     */
    public List<Preinscrit> toEntityList(List<PreinscritDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour une entité Preinscrit existante avec les données d'un PreinscritDTO
     *
     * @param entity l'entité à mettre à jour
     * @param dto les données à appliquer
     * @return l'entité mise à jour
     */
    public Preinscrit updateEntityFromDTO(Preinscrit entity, PreinscritDTO dto) {
        if (entity == null || dto == null) {
            return entity;
        }

        // Ne pas modifier l'ID
        if (dto.getDatePreInscription() != null) {
            entity.setDatePreInscription(dto.getDatePreInscription());
        }
        if (dto.getTitre() != null) {
            entity.setTitre(dto.getTitre());
        }
        if (dto.getNom() != null) {
            entity.setNom(dto.getNom());
        }
        if (dto.getPrenom() != null) {
            entity.setPrenom(dto.getPrenom());
        }
        if (dto.getAdresse() != null) {
            entity.setAdresse(dto.getAdresse());
        }
        if (dto.getCodePostal() != null) {
            entity.setCodePostal(dto.getCodePostal());
        }
        if (dto.getVille() != null) {
            entity.setVille(dto.getVille());
        }
        if (dto.getTelDomicile() != null) {
            entity.setTelDomicile(dto.getTelDomicile());
        }
        if (dto.getTelPortable() != null) {
            entity.setTelPortable(dto.getTelPortable());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getSexe() != null) {
            entity.setSexe(dto.getSexe());
        }
        if (dto.getDateNaissance() != null) {
            entity.setDateNaissance(dto.getDateNaissance());
        }
        if (dto.getPhototype() != null) {
            entity.setPhototype(dto.getPhototype());
        }
        if (dto.getPeauSensible() != null) {
            entity.setPeauSensible(dto.getPeauSensible());
        }
        if (dto.getMapyeux() != null) {
            entity.setMapyeux(dto.getMapyeux());
        }
        if (dto.getMaplevres() != null) {
            entity.setMaplevres(dto.getMaplevres());
        }
        if (dto.getMapsourcils() != null) {
            entity.setMapsourcils(dto.getMapsourcils());
        }
        if (dto.getPeauVisage() != null) {
            entity.setPeauVisage(dto.getPeauVisage());
        }
        if (dto.getRideVisage() != null) {
            entity.setRideVisage(dto.getRideVisage());
        }
        if (dto.getTachePigmentaireVisage() != null) {
            entity.setTachePigmentaireVisage(dto.getTachePigmentaireVisage());
        }
        if (dto.getBoutonsVisage() != null) {
            entity.setBoutonsVisage(dto.getBoutonsVisage());
        }
        if (dto.getComedonsVisage() != null) {
            entity.setComedonsVisage(dto.getComedonsVisage());
        }
        if (dto.getPoresDilatesVisage() != null) {
            entity.setPoresDilatesVisage(dto.getPoresDilatesVisage());
        }
        if (dto.getPochesYeuxVisage() != null) {
            entity.setPochesYeuxVisage(dto.getPochesYeuxVisage());
        }
        if (dto.getCernesVisage() != null) {
            entity.setCernesVisage(dto.getCernesVisage());
        }
        if (dto.getEthnie() != null) {
            entity.setEthnie(dto.getEthnie());
        }

        // Date et heure de rendez-vous peuvent être null
        entity.setRdvDate(dto.getRdvDate());
        entity.setRdvHeure(dto.getRdvHeure());

        if (dto.getEtat() != null) {
            entity.setEtat(dto.getEtat());
        }
        if (dto.getCommentaires() != null) {
            entity.setCommentaires(dto.getCommentaires());
        }

        return entity;
    }
}