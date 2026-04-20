package com.example.cosmetest.utils;

import com.example.cosmetest.business.dto.VolontaireDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilitaire pour faciliter la manipulation des objets par réflexion
 */
public class ReflectionUtils {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);
    /**
     * Convertit un objet DTO en Map avec des vérifications de sécurité pour les IDs
     *
     * @param dto L'objet DTO à convertir
     * @return Une Map contenant les propriétés de l'objet avec des noms adaptés au frontend
     */
    public static Map<String, Object> convertDtoToFrontendMap(Object dto) {
        Map<String, Object> result = new HashMap<>();

        if (dto == null) {
            return result;
        }

        try {
            // Récupérer toutes les méthodes "getter" de l'objet
            Method[] methods = dto.getClass().getMethods();
            for (Method method : methods) {
                String methodName = method.getName();

                // Vérifier si c'est un getter (commence par "get" et n'a pas de paramètres)
                if (methodName.startsWith("get") && method.getParameterCount() == 0
                        && !methodName.equals("getClass")) {

                    // Récupérer le nom de la propriété (enlever le "get" et mettre la première lettre en minuscule)
                    String propertyName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);

                    // Cas spécial pour la propriété ID
                    if (propertyName.equals("idVol")) {
                        Object idValue = method.invoke(dto);
                        if (idValue != null && (Integer)idValue > 0) {
                            // Renommer idVol en id pour le frontend
                            result.put("id", idValue);
                        }
                    } else {
                        // Pour toutes les autres propriétés
                        Object value = method.invoke(dto);
                        if (value != null) {
                            // Adapter le nom si nécessaire (par exemple, nomVol -> nom)
                            String frontendPropertyName = adaptPropertyNameForFrontend(propertyName);
                            result.put(frontendPropertyName, value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la conversion DTO -> Map: {}", e.getMessage());
        }

        return result;
    }

    /**
     * Adapte le nom d'une propriété du backend pour le frontend
     */
    private static String adaptPropertyNameForFrontend(String backendName) {
        // Exemples de transformation
        if (backendName.endsWith("Vol")) {
            return backendName.substring(0, backendName.length() - 3);
        }

        return backendName;
    }

    /**
     * Méthode spécifique pour extraire l'ID sécurisé d'un volontaire
     */
    public static Integer getSecureVolontaireId(VolontaireDTO volontaireDTO) {
        if (volontaireDTO == null) {
            return null;
        }

        Integer id = volontaireDTO.getIdVol();
        if (id == null || id <= 0) {
            return null;
        }

        return id;
    }
}