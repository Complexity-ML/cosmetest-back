package com.example.cosmetest.business.mapper;

import com.example.cosmetest.business.dto.EtudeDTO;
import com.example.cosmetest.domain.model.Etude;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre les entités Etude et les DTOs
 * Cette classe fait partie de la couche BLL et assure la transformation
 * des objets entre la couche de données et la couche de présentation
 */
@Component
public class EtudeMapper {

    /**
     * Convertit une entité Etude en DTO
     * @param etude Entité Etude
     * @return DTO correspondant
     */
    public EtudeDTO toDto(Etude etude) {
        if (etude == null) {
            return null;
        }

        EtudeDTO dto = new EtudeDTO();
        dto.setIdEtude(etude.getIdEtude());
        dto.setRef(etude.getRef());
        dto.setType(etude.getType());
        dto.setProduits(etude.getProduits());
        dto.setTitre(etude.getTitre());
        dto.setDateDebut(etude.getDateDebut());
        dto.setDateFin(etude.getDateFin());
        dto.setWashout(etude.getWashout());

        // Mettre à jour pour gérer les nouveaux champs
        // Prioriser description, mais conserver commentaires pour compatibilité
        dto.setCommentaires(etude.getCommentaires());

        dto.setExamens(etude.getExamens());

        // Utiliser capaciteVolontaires et garder nbSujets pour compatibilité
        dto.setCapaciteVolontaires(etude.getCapaciteVolontaires());
        dto.setNbSujets(etude.getNbSujets());

        dto.setPaye(etude.getPaye());
        dto.setArchive(etude.isArchive());

        return dto;
    }

    /**
     * Convertit un DTO en entité Etude
     * @param dto DTO à convertir
     * @return Entité Etude correspondante
     */
    public Etude toEntity(EtudeDTO dto) {
        if (dto == null) {
            return null;
        }

        Etude etude = new Etude();
        etude.setIdEtude(dto.getIdEtude());
        etude.setRef(dto.getRef());
        etude.setType(dto.getType());
        etude.setProduits(dto.getProduits());
        etude.setTitre(dto.getTitre());
        etude.setDateDebut(dto.getDateDebut());
        etude.setDateFin(dto.getDateFin());
        etude.setWashout(dto.getWashout());

        // Mettre à jour pour gérer les nouveaux champs
        // Prioriser description, backfall sur commentaires si nécessaire
        etude.setCommentaires(dto.getCommentaires());

        etude.setExamens(dto.getExamens());

        // Utiliser capaciteVolontaires s'il est disponible, sinon utiliser nbSujets
        if (dto.getCapaciteVolontaires() != null) {
            etude.setCapaciteVolontaires(dto.getCapaciteVolontaires());
        } else if (dto.getNbSujets() != null) {
            etude.setNbSujets(dto.getNbSujets());
        }

        etude.setPaye(dto.getPaye());
        etude.setArchive(dto.isArchive());

        return etude;
    }

    /**
     * Met à jour une entité existante avec les valeurs du DTO
     * @param dto DTO contenant les nouvelles valeurs
     * @param etude Entité à mettre à jour
     * @return Entité mise à jour
     */
    public Etude updateEntityFromDto(EtudeDTO dto, Etude etude) {
        if (dto == null || etude == null) {
            return etude;
        }

        // Ne pas mettre à jour l'ID car c'est la clé primaire générée automatiquement
        etude.setRef(dto.getRef());
        etude.setType(dto.getType());
        etude.setProduits(dto.getProduits());
        etude.setTitre(dto.getTitre());
        etude.setDateDebut(dto.getDateDebut());
        etude.setDateFin(dto.getDateFin());
        etude.setWashout(dto.getWashout());

        // Mettre à jour pour gérer les nouveaux champs
        // Prioriser description, backfall sur commentaires si nécessaire
        etude.setCommentaires(dto.getCommentaires());

        etude.setExamens(dto.getExamens());

        // Utiliser capaciteVolontaires s'il est disponible, sinon utiliser nbSujets
        if (dto.getCapaciteVolontaires() != null) {
            etude.setCapaciteVolontaires(dto.getCapaciteVolontaires());
        } else if (dto.getNbSujets() != null) {
            etude.setNbSujets(dto.getNbSujets());
        }

        etude.setPaye(dto.getPaye());
        etude.setArchive(dto.isArchive());

        return etude;
    }
}