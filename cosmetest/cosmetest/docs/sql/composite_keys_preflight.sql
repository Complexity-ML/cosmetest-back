-- Préflight global non destructif avant remplacement des clés composites.
-- Tables : etude_volontaire, rdv, infobancaire, preetudevol.
-- À exécuter sur la COPIE LOCALE après un dump vérifié.
-- Ce script n'effectue aucun INSERT/UPDATE/DELETE/ALTER/DROP.

SET SESSION TRANSACTION READ ONLY;
START TRANSACTION WITH CONSISTENT SNAPSHOT;

SELECT DATABASE() AS database_name, VERSION() AS mysql_version, NOW() AS captured_at;

-- Schéma réel : colonnes, PK, index, FK et contraintes.
SELECT TABLE_NAME, COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE, COLUMN_DEFAULT, EXTRA,
       CHARACTER_SET_NAME, COLLATION_NAME
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME IN ('etude_volontaire', 'rdv', 'infobancaire', 'preetudevol', 'annulation')
ORDER BY TABLE_NAME, ORDINAL_POSITION;

SELECT TABLE_NAME, INDEX_NAME, NON_UNIQUE, SEQ_IN_INDEX, COLUMN_NAME
FROM information_schema.STATISTICS
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME IN ('etude_volontaire', 'rdv', 'infobancaire', 'preetudevol', 'annulation')
ORDER BY TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX;

SELECT tc.TABLE_NAME, tc.CONSTRAINT_NAME, tc.CONSTRAINT_TYPE,
       kcu.COLUMN_NAME, kcu.REFERENCED_TABLE_NAME, kcu.REFERENCED_COLUMN_NAME
FROM information_schema.TABLE_CONSTRAINTS tc
LEFT JOIN information_schema.KEY_COLUMN_USAGE kcu
  ON kcu.CONSTRAINT_SCHEMA = tc.CONSTRAINT_SCHEMA
 AND kcu.TABLE_NAME = tc.TABLE_NAME
 AND kcu.CONSTRAINT_NAME = tc.CONSTRAINT_NAME
WHERE tc.CONSTRAINT_SCHEMA = DATABASE()
  AND tc.TABLE_NAME IN ('etude_volontaire', 'rdv', 'infobancaire', 'preetudevol', 'annulation')
ORDER BY tc.TABLE_NAME, tc.CONSTRAINT_NAME, kcu.ORDINAL_POSITION;

SHOW CREATE TABLE etude_volontaire;
SHOW CREATE TABLE rdv;
SHOW CREATE TABLE infobancaire;
SHOW CREATE TABLE preetudevol;
SHOW CREATE TABLE annulation;

SELECT 'etude_volontaire' AS table_name, COUNT(*) AS total_rows FROM etude_volontaire
UNION ALL SELECT 'rdv', COUNT(*) FROM rdv
UNION ALL SELECT 'infobancaire', COUNT(*) FROM infobancaire
UNION ALL SELECT 'preetudevol', COUNT(*) FROM preetudevol
UNION ALL SELECT 'annulation', COUNT(*) FROM annulation;

-- ETUDE_VOLONTAIRE : conflits de participation et conflits financiers.
SELECT ID_ETUDE, ID_VOLONTAIRE,
       COUNT(*) AS duplicate_count,
       COUNT(DISTINCT ID_GROUPE) AS distinct_groups,
       COUNT(DISTINCT IV) AS distinct_amounts,
       COUNT(DISTINCT NUMSUJET) AS distinct_subjects,
       COUNT(DISTINCT PAYE) AS distinct_payment_states,
       COUNT(DISTINCT STATUT) AS distinct_statuses
FROM etude_volontaire
GROUP BY ID_ETUDE, ID_VOLONTAIRE
HAVING COUNT(*) > 1
ORDER BY duplicate_count DESC, ID_ETUDE, ID_VOLONTAIRE;

SELECT ID_ETUDE, NUMSUJET, COUNT(*) AS duplicate_count
FROM etude_volontaire
WHERE NUMSUJET > 0
GROUP BY ID_ETUDE, NUMSUJET
HAVING COUNT(*) > 1
ORDER BY duplicate_count DESC, ID_ETUDE, NUMSUJET;

SELECT *
FROM etude_volontaire
WHERE ID_VOLONTAIRE IS NULL OR ID_VOLONTAIRE = 0
   OR ID_GROUPE IS NULL OR ID_GROUPE = 0
   OR PAYE NOT IN (0, 1)
   OR IV < 0
   OR NUMSUJET < 0;

SELECT ev.*
FROM etude_volontaire ev
LEFT JOIN etude e ON e.ID_ETUDE = ev.ID_ETUDE
WHERE e.ID_ETUDE IS NULL;

SELECT ev.*
FROM etude_volontaire ev
LEFT JOIN volontaire v ON v.ID_VOL = ev.ID_VOLONTAIRE
WHERE ev.ID_VOLONTAIRE IS NOT NULL AND ev.ID_VOLONTAIRE <> 0 AND v.ID_VOL IS NULL;

SELECT ev.*
FROM etude_volontaire ev
JOIN groupe g ON g.ID_GROUPE = ev.ID_GROUPE
WHERE ev.ID_GROUPE IS NOT NULL AND ev.ID_GROUPE <> 0
  AND g.ID_ETUDE <> ev.ID_ETUDE;

-- RDV : orphelins et ambiguïtés de la référence logique annulation -> RDV.
SELECT ID_ETUDE, ID_RDV, COUNT(*) AS duplicate_count
FROM rdv
GROUP BY ID_ETUDE, ID_RDV
HAVING COUNT(*) > 1;

SELECT r.*
FROM rdv r
LEFT JOIN etude e ON e.ID_ETUDE = r.ID_ETUDE
WHERE e.ID_ETUDE IS NULL;

SELECT r.*
FROM rdv r
LEFT JOIN volontaire v ON v.ID_VOL = r.ID_VOLONTAIRE
WHERE r.ID_VOLONTAIRE IS NOT NULL AND v.ID_VOL IS NULL;

SELECT a.ID_ETUDE, a.ID_RDV, COUNT(r.ID_RDV) AS matching_rdvs
FROM annulation a
LEFT JOIN rdv r ON r.ID_ETUDE = a.ID_ETUDE AND r.ID_RDV = a.ID_RDV
GROUP BY a.ID_ETUDE, a.ID_RDV
HAVING COUNT(r.ID_RDV) <> 1;

-- INFOBANCAIRE : unicité métier et orphelins.
SELECT BIC, IBAN, ID_VOL, COUNT(*) AS duplicate_count
FROM infobancaire
GROUP BY BIC, IBAN, ID_VOL
HAVING COUNT(*) > 1;

SELECT ib.*
FROM infobancaire ib
LEFT JOIN volontaire v ON v.ID_VOL = ib.ID_VOL
WHERE v.ID_VOL IS NULL;

-- PREETUDEVOL : unicité métier, parents et cohérence groupe/étude.
SELECT ID_ETUDE, ID_GROUPE, ID_VOLONTAIRE, COUNT(*) AS duplicate_count
FROM preetudevol
GROUP BY ID_ETUDE, ID_GROUPE, ID_VOLONTAIRE
HAVING COUNT(*) > 1;

SELECT p.*
FROM preetudevol p
LEFT JOIN etude e ON e.ID_ETUDE = p.ID_ETUDE
LEFT JOIN groupe g ON g.ID_GROUPE = p.ID_GROUPE
LEFT JOIN volontaire v ON v.ID_VOL = p.ID_VOLONTAIRE
WHERE e.ID_ETUDE IS NULL OR g.ID_GROUPE IS NULL OR v.ID_VOL IS NULL;

SELECT p.*
FROM preetudevol p
JOIN groupe g ON g.ID_GROUPE = p.ID_GROUPE
WHERE g.ID_ETUDE <> p.ID_ETUDE;

COMMIT;
