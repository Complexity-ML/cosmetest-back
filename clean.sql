START TRANSACTION;

DELETE ev
FROM etude_volontaire ev
LEFT JOIN etude e ON ev.ID_ETUDE = e.ID_ETUDE
WHERE e.ID_ETUDE IS NULL;

DELETE ev
FROM etude_volontaire ev
LEFT JOIN groupe g ON ev.ID_GROUPE = g.ID_GROUPE
WHERE g.ID_GROUPE IS NULL;

DELETE ev
FROM etude_volontaire ev
LEFT JOIN volontaire v ON ev.ID_VOLONTAIRE = v.ID_VOL
WHERE v.ID_VOL IS NULL;

COMMIT;


-- 1) Désactiver les vérifications strictes
SET @OLD_SQL_MODE = @@SQL_MODE;
SET SESSION SQL_MODE = REPLACE(REPLACE(REPLACE(@OLD_SQL_MODE,
        'STRICT_TRANS_TABLES',''),
        'NO_ZERO_DATE',''),
        'NO_ZERO_IN_DATE','');

-- 2) Modifier la colonne pour accepter NULL
ALTER TABLE volontaire
MODIFY COLUMN DATE_NAISSANCE DATE NULL;

-- 3) Remplacer les 0000-00-00 par NULL
UPDATE volontaire
SET DATE_NAISSANCE = NULL
WHERE DATE_NAISSANCE = '0000-00-00';

-- 4) (Optionnel) Repasser la colonne en NOT NULL si tu veux
-- ALTER TABLE volontaire
-- MODIFY COLUMN DATE_NAISSANCE DATE NOT NULL;

-- 5) Restaurer le SQL_MODE d’origine
SET SESSION SQL_MODE = @OLD_SQL_MODE;


-- 1. Sauvegarder le SQL_MODE courant et retirer les options bloquantes
SET @OLD_SQL_MODE = @@SQL_MODE;
SET SESSION SQL_MODE = REPLACE(REPLACE(REPLACE(@OLD_SQL_MODE,
        'STRICT_TRANS_TABLES',''),
        'NO_ZERO_DATE',''),
        'NO_ZERO_IN_DATE','');

-- 2. (si nécessaire) autoriser NULL sur DATE_I
ALTER TABLE volontaire
MODIFY COLUMN DATE_I DATE NULL;

-- 3. Mettre les dates nulles à zéro à NULL (ou à une vraie date)
UPDATE volontaire
SET DATE_I = NULL
WHERE DATE_I = '0000-00-00';

-- 4. (optionnel) repasser la colonne en NOT NULL si tu veux
-- ALTER TABLE volontaire
-- MODIFY COLUMN DATE_I DATE NOT NULL;

-- 5. Restaurer le SQL_MODE d’origine
SET SESSION SQL_MODE = @OLD_SQL_MODE;


ALTER TABLE volontaire ENGINE = InnoDB;
ALTER TABLE annulation ENGINE = InnoDB;
ALTER TABLE infobancaire ENGINE = InnoDB;
ALTER TABLE rdv ENGINE = InnoDB;
ALTER TABLE volbug ENGINE = InnoDB;
ALTER TABLE volontaire_hc ENGINE = InnoDB;


