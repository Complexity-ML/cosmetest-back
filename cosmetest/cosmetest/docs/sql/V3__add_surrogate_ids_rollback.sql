-- Rollback de V3__add_surrogate_ids.sql.
-- À exécuter uniquement en maintenance après vérification de l'unicité RDV historique.

ALTER TABLE preetudevol
    DROP PRIMARY KEY,
    DROP COLUMN ID_PREETUDEVOL;

ALTER TABLE infobancaire
    DROP PRIMARY KEY,
    DROP COLUMN ID_INFOBANCAIRE;

ALTER TABLE rdv
    DROP PRIMARY KEY,
    DROP INDEX uk_rdv_etude_numero,
    DROP COLUMN RDV_PK,
    ADD PRIMARY KEY (ID_ETUDE, ID_RDV);

ALTER TABLE etude_volontaire
    DROP PRIMARY KEY,
    DROP COLUMN ID_ETUDE_VOLONTAIRE;
