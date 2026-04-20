-- Ensure rdv.ID_VOLONTAIRE respects referential integrity and define FK with ON DELETE SET NULL

-- 1) Make sure the column is nullable (required for ON DELETE SET NULL)
ALTER TABLE rdv MODIFY ID_VOLONTAIRE INT NULL;

-- 2) Nullify orphan references
UPDATE rdv r
LEFT JOIN volontaire v ON v.ID_VOL = r.ID_VOLONTAIRE
SET r.ID_VOLONTAIRE = NULL
WHERE r.ID_VOLONTAIRE IS NOT NULL
  AND v.ID_VOL IS NULL;

-- 3) Add the foreign key if not present
SET @fk_name := 'fk_rdv_volontaire';
SET @schema_name := DATABASE();

SELECT COUNT(*) INTO @fk_exists
FROM information_schema.TABLE_CONSTRAINTS
WHERE CONSTRAINT_SCHEMA = @schema_name
  AND TABLE_NAME = 'rdv'
  AND CONSTRAINT_NAME = @fk_name
  AND CONSTRAINT_TYPE = 'FOREIGN KEY';

SET @ddl := IF(@fk_exists = 0,
  'ALTER TABLE rdv\n    ADD CONSTRAINT fk_rdv_volontaire\n      FOREIGN KEY (ID_VOLONTAIRE) REFERENCES volontaire(ID_VOL)\n      ON DELETE SET NULL',
  'DO 0');

PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

