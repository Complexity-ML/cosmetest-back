-- Diagnostic non destructif avant refonte de la clé etude_volontaire.
-- Exécuter manuellement sur MySQL après sauvegarde. Ce script ne modifie aucune donnée.

SHOW CREATE TABLE etude_volontaire;

SELECT COUNT(*) AS total_rows
FROM etude_volontaire;

-- Vérifie la règle métier candidate : une participation par étude et volontaire.
SELECT
    ID_ETUDE,
    ID_VOLONTAIRE,
    COUNT(*) AS duplicate_count,
    COUNT(DISTINCT ID_GROUPE) AS distinct_groups,
    COUNT(DISTINCT IV) AS distinct_iv,
    COUNT(DISTINCT NUMSUJET) AS distinct_subject_numbers,
    COUNT(DISTINCT PAYE) AS distinct_payment_states,
    COUNT(DISTINCT STATUT) AS distinct_statuses
FROM etude_volontaire
GROUP BY ID_ETUDE, ID_VOLONTAIRE
HAVING COUNT(*) > 1
ORDER BY duplicate_count DESC, ID_ETUDE, ID_VOLONTAIRE;

-- Détaille les doublons afin de définir une règle de canonicalisation sans perte financière.
SELECT ev.*
FROM etude_volontaire ev
JOIN (
    SELECT ID_ETUDE, ID_VOLONTAIRE
    FROM etude_volontaire
    GROUP BY ID_ETUDE, ID_VOLONTAIRE
    HAVING COUNT(*) > 1
) duplicates
  ON duplicates.ID_ETUDE = ev.ID_ETUDE
 AND duplicates.ID_VOLONTAIRE = ev.ID_VOLONTAIRE
ORDER BY ev.ID_ETUDE, ev.ID_VOLONTAIRE, ev.PAYE DESC, ev.NUMSUJET DESC;

-- Recherche les relations orphelines avant ajout des futures clés étrangères.
SELECT ev.*
FROM etude_volontaire ev
LEFT JOIN etude e ON e.ID_ETUDE = ev.ID_ETUDE
WHERE e.ID_ETUDE IS NULL;

SELECT ev.*
FROM etude_volontaire ev
LEFT JOIN volontaire v ON v.ID_VOL = ev.ID_VOLONTAIRE
WHERE v.ID_VOL IS NULL;

SELECT ev.*
FROM etude_volontaire ev
LEFT JOIN groupe g ON g.ID_GROUPE = ev.ID_GROUPE
WHERE ev.ID_GROUPE IS NOT NULL
  AND ev.ID_GROUPE <> 0
  AND g.ID_GROUPE IS NULL;
