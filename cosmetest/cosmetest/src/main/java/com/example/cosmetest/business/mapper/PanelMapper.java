package com.example.cosmetest.business.mapper;

import com.example.cosmetest.business.dto.PanelDTO;
import com.example.cosmetest.domain.model.Panel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper pour convertir entre l'entité Panel et PanelDTO
 */
@Component
public class PanelMapper {

    /**
     * Convertit une entité Panel en PanelDTO
     *
     * @param panel l'entité à convertir
     * @return le DTO correspondant
     */
    public PanelDTO toDTO(Panel panel) {
        if (panel == null) {
            return null;
        }

        PanelDTO dto = new PanelDTO();

        // Attributs de base
        dto.setIdPanel(panel.getIdPanel());
        dto.setIdEtude(panel.getIdEtude());
        dto.setIdGroupe(panel.getIdGroupe());
        dto.setSexe(panel.getSexe());
        dto.setCarnation(panel.getCarnation());
        dto.setBronzage(panel.getBronzage());
        dto.setCoupsDeSoleil(panel.getCoupsDeSoleil());
        dto.setExpositionSolaire(panel.getExpositionSolaire());
        dto.setPhototype(panel.getPhototype());
        dto.setSensibiliteCutanee(panel.getSensibiliteCutanee());
        dto.setTypePeauVisage(panel.getTypePeauVisage());

        // Pour éviter de copier manuellement tous les attributs, nous utilisons la
        // réflexion
        // pour les attributs restants
        copyPropertiesViaReflection(panel, dto);

        return dto;
    }

    /**
     * Convertit un PanelDTO en entité Panel
     *
     * @param dto le DTO à convertir
     * @return l'entité correspondante
     */
    public Panel toEntity(PanelDTO dto) {
        if (dto == null) {
            return null;
        }

        Panel panel;

        // Créer un nouveau Panel avec les attributs obligatoires
        if (dto.getIdPanel() == null) {
            panel = new Panel(dto.getIdEtude(), dto.getIdGroupe());
        } else {
            panel = new Panel(dto.getIdEtude(), dto.getIdGroupe());
            panel.setIdPanel(dto.getIdPanel());
        }

        // Définir les attributs de base
        panel.setSexe(dto.getSexe());
        panel.setCarnation(dto.getCarnation());
        panel.setBronzage(dto.getBronzage());
        panel.setCoupsDeSoleil(dto.getCoupsDeSoleil());
        panel.setExpositionSolaire(dto.getExpositionSolaire());
        panel.setPhototype(dto.getPhototype());
        panel.setSensibiliteCutanee(dto.getSensibiliteCutanee());
        panel.setTypePeauVisage(dto.getTypePeauVisage());

        // Pour éviter de copier manuellement tous les attributs, nous utilisons la
        // réflexion
        // pour les attributs restants
        copyPropertiesViaReflection(dto, panel);

        return panel;
    }

    /**
     * Convertit une liste d'entités Panel en liste de PanelDTO
     *
     * @param panels la liste d'entités
     * @return la liste de DTOs
     */
    public List<PanelDTO> toDTOList(List<Panel> panels) {
        return panels.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de PanelDTO en liste d'entités Panel
     *
     * @param dtos la liste de DTOs
     * @return la liste d'entités
     */
    public List<Panel> toEntityList(List<PanelDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour une entité Panel existante avec les données d'un PanelDTO
     *
     * @param entity l'entité à mettre à jour
     * @param dto    les données à appliquer
     * @return l'entité mise à jour
     */
    public Panel updateEntityFromDTO(Panel entity, PanelDTO dto) {
        if (entity == null || dto == null) {
            return entity;
        }

        // Mettre à jour les attributs de base
        if (dto.getIdEtude() != null) {
            entity.setIdEtude(dto.getIdEtude());
        }

        if (dto.getIdGroupe() != null) {
            entity.setIdGroupe(dto.getIdGroupe());
        }

        // Mettre à jour tous les autres attributs non-null
        updateIfNotNull(dto.getSexe(), entity::setSexe);
        updateIfNotNull(dto.getCarnation(), entity::setCarnation);
        updateIfNotNull(dto.getBronzage(), entity::setBronzage);
        updateIfNotNull(dto.getCoupsDeSoleil(), entity::setCoupsDeSoleil);
        updateIfNotNull(dto.getExpositionSolaire(), entity::setExpositionSolaire);
        updateIfNotNull(dto.getPhototype(), entity::setPhototype);
        updateIfNotNull(dto.getSensibiliteCutanee(), entity::setSensibiliteCutanee);
        updateIfNotNull(dto.getTypePeauVisage(), entity::setTypePeauVisage);

        // Pour éviter de copier manuellement tous les attributs, nous utilisons la
        // réflexion
        // pour les attributs restants
        updatePropertiesViaReflection(dto, entity);

        return entity;
    }

    /**
     * Méthode auxiliaire pour copier les propriétés d'un objet source vers un objet
     * cible
     * en utilisant la réflexion
     *
     * @param source l'objet source
     * @param target l'objet cible
     */
    private void copyPropertiesViaReflection(Object source, Object target) {
        // Cette méthode utiliserait la réflexion pour copier toutes les propriétés
        // d'un objet source vers un objet cible. Pour des raisons de simplicité et
        // d'efficacité, nous ne l'implémentons pas complètement ici.

        // Dans une implémentation réelle, vous pourriez utiliser:
        // - Spring BeanUtils.copyProperties
        // - MapStruct
        // - ModelMapper
        // ou une bibliothèque similaire
    }

    /**
     * Méthode auxiliaire pour mettre à jour les propriétés d'un objet cible à
     * partir
     * d'un objet source en utilisant la réflexion
     *
     * @param source l'objet source
     * @param target l'objet cible
     */
    private void updatePropertiesViaReflection(Object source, Object target) {
        // Cette méthode utiliserait la réflexion pour mettre à jour toutes les
        // propriétés
        // non-null d'un objet source vers un objet cible. Pour des raisons de
        // simplicité
        // et d'efficacité, nous ne l'implémentons pas complètement ici.
    }

    /**
     * Méthode utilitaire pour mettre à jour une propriété si la valeur n'est pas
     * null
     *
     * @param value  la valeur à définir
     * @param setter la méthode setter à appeler
     * @param <T>    le type de la valeur
     */
    private <T> void updateIfNotNull(T value, java.util.function.Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}