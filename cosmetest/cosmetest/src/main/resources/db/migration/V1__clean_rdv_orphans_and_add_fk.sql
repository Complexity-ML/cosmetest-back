-- Clean orphan RDV rows and add FK with ON DELETE CASCADE

-- 1) Remove RDVs whose ID_ETUDE does not exist in etude
DELETE r
FROM rdv r
LEFT JOIN etude e ON e.ID_ETUDE = r.ID_ETUDE
WHERE r.ID_ETUDE IS NOT NULL
  AND e.ID_ETUDE IS NULL;

-- 2) Add the foreign key if it does not already exist, using a stable name
SET @fk_name := 'fk_rdv_etude';
SET @schema_name := DATABASE();

SELECT COUNT(*) INTO @fk_exists
FROM information_schema.TABLE_CONSTRAINTS
WHERE CONSTRAINT_SCHEMA = @schema_name
  AND TABLE_NAME = 'rdv'
  AND CONSTRAINT_NAME = @fk_name
  AND CONSTRAINT_TYPE = 'FOREIGN KEY';

SET @ddl := IF(@fk_exists = 0,
  'ALTER TABLE rdv\n    ADD CONSTRAINT fk_rdv_etude\n      FOREIGN KEY (ID_ETUDE) REFERENCES etude(ID_ETUDE)\n      ON DELETE CASCADE',
  'DO 0');

PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

