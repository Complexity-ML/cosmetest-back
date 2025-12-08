-- Migration pour ajouter le champ annule_par à la table annulation
-- Permet de distinguer si l'annulation a été faite par Cosmetest ou par le Volontaire

-- Ajouter la colonne annule_par
ALTER TABLE annulation
ADD COLUMN annule_par VARCHAR(20) NULL;

-- Mettre une valeur par défaut pour les annulations existantes
UPDATE annulation
SET annule_par = 'COSMETEST'
WHERE annule_par IS NULL;

-- Ajouter un commentaire sur la colonne pour la documentation
COMMENT ON COLUMN annulation.annule_par IS 'Indique qui a annulé : COSMETEST ou VOLONTAIRE';
