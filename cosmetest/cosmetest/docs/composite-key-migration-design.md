# Migration des quatre clés composites vers des identifiants techniques

Date : 2026-07-14  
Périmètre : `etude_volontaire`, `rdv`, `infobancaire`, `preetudevol`.

## Décision

Les quatre tables migreront vers une clé primaire technique `BIGINT AUTO_INCREMENT`. Les anciennes colonnes restent des données métier et conservent les contraintes d'unicité nécessaires. La migration suit un modèle **expand / migrate / contract** : aucune ancienne clé n'est supprimée avant que le backend, le frontend et les données aient été validés.

Aucun DDL ne doit être confié à `spring.jpa.hibernate.ddl-auto=update`. La production utilise MySQL/WampServer; un dump restaurable et une fenêtre de maintenance sont obligatoires.

## État actuel confirmé

| Table | Clé actuelle | Problème principal |
|---|---|---|
| `etude_volontaire` | 7 colonnes : étude, groupe, volontaire, IV, numéro sujet, payé, statut | quatre champs mutables font partie de l'identité; les updates deviennent delete/reinsert et les doublons financiers sont possibles |
| `rdv` | `(ID_ETUDE, ID_RDV)` | `ID_RDV` mélange numéro métier par étude, URL et identité persistante |
| `infobancaire` | `(BIC, IBAN, ID_VOL)` | toute modification bancaire change l'identité; des données bancaires sont présentes dans les routes historiques |
| `preetudevol` | `(ID_ETUDE, ID_GROUPE, ID_VOLONTAIRE)` | aucune identité stable et cohérence groupe/étude non garantie en base |

## Schémas cibles

### `etude_volontaire`

- PK : `ID_ETUDE_VOLONTAIRE BIGINT AUTO_INCREMENT`.
- Unique métier : `(ID_ETUDE, ID_VOLONTAIRE)`, uniquement après résolution des lignes `ID_VOLONTAIRE=0/NULL` et des doublons.
- Colonnes ordinaires : `ID_GROUPE`, `IV`, `NUMSUJET`, `PAYE`, `STATUT`.
- Invariants : `PAYE IN (0,1)`, `IV >= 0`, `NUMSUJET >= 0`.
- Numéro sujet strictement positif unique par étude, à réaliser avec une colonne générée nullable + index unique compatible MySQL.
- Aucun conflit divergent de paiement ou montant ne sera fusionné automatiquement.

### `rdv`

- PK : `RDV_ID BIGINT AUTO_INCREMENT`, global et purement technique.
- `NUMERO_RDV` conserve l'ancien `ID_RDV` si le numéro métier doit rester visible.
- Unique transitoire/métier : `(ID_ETUDE, NUMERO_RDV)`.
- `annulation.RDV_ID` nullable est rétroalimenté depuis l'ancien couple `(ID_ETUDE, ID_RDV)`.
- La FK d'annulation utilise `RESTRICT`; l'ancien RDV annulé reste conservé.
- Les remplacements reçoivent un nouvel ID technique; la règle de leur numéro métier reste celle de l'allocateur tant qu'elle n'est pas explicitement retirée.

### `infobancaire`

- PK : `ID_INFOBANCAIRE BIGINT AUTO_INCREMENT`.
- Unique métier conservé : `(BIC, IBAN, ID_VOL)`.
- FK recommandée après correction des orphelins : `ID_VOL -> volontaire.ID_VOL`.
- Pas de `UNIQUE(ID_VOL)` ni de `UNIQUE(IBAN)` sans nouvelle règle métier.
- Pas de cascade de suppression implicite sur les informations bancaires.

### `preetudevol`

- PK : `ID_PREETUDEVOL BIGINT AUTO_INCREMENT`.
- Unique métier conservé : `(ID_ETUDE, ID_GROUPE, ID_VOLONTAIRE)`.
- FK vers étude, groupe et volontaire après correction des orphelins.
- Contrôle obligatoire que le groupe appartient à l'étude.

## Portes de sécurité

La migration s'arrête si l'un des points suivants n'est pas résolu :

1. doublon `etude_volontaire` avec divergence de `PAYE` ou `IV`;
2. numéro sujet positif dupliqué dans une étude;
3. `ID_VOLONTAIRE=0` ou groupe `0` sans règle métier validée;
4. annulation ne retrouvant pas exactement un RDV historique;
5. pré-étude dont le groupe appartient à une autre étude;
6. référence orpheline;
7. restauration du dump non testée;
8. schéma réel différent des noms/types supposés.

## Séquence d'exécution

### Phase 0 — préflight et sauvegarde

1. Exécuter `docs/sql/composite_keys_preflight.sql` sur la copie locale.
2. Exporter `SHOW CREATE TABLE`, index, FK, triggers et collations.
3. Créer un dump avec routines et triggers.
4. Restaurer ce dump dans une base temporaire et comparer les nombres de lignes.
5. Classer chaque anomalie : automatique sûre, revue métier, ou blocante.

### Phase 1 — expansion SQL

Dans une migration versionnée séparée par table :

1. ajouter l'ID technique sans retirer les colonnes historiques;
2. remplir et vérifier l'unicité des nouveaux IDs;
3. ajouter les colonnes de transition (`NUMERO_RDV`, `annulation.RDV_ID`);
4. créer les contraintes uniques seulement après canonicalisation;
5. archiver les lignes supprimées/fusionnées dans des tables horodatées;
6. produire un script rollback correspondant.

Le DDL MySQL fait des commits implicites : `START TRANSACTION` n'est pas un mécanisme de rollback suffisant.

### Phase 2 — backend transitoire

- Ajouter les nouveaux IDs aux entités et DTO sans retirer immédiatement les anciennes routes.
- Repositories typés `Long` après bascule de l'identité JPA.
- Remplacer les expressions `entity.id.*` par des propriétés métier ordinaires.
- Remplacer les delete/reinsert par des updates en place.
- Ignorer tout ID technique fourni lors d'une création.
- Traduire les collisions de contraintes en `409` neutre.
- Maintenir temporairement les recherches par anciennes clés comme compatibilité.

### Phase 3 — API et frontend

- Nouvelles routes principales `GET/PATCH/DELETE /{id}`.
- `EtudeVolontaire` : les modifications envoient l'ID technique et la seule valeur modifiée.
- `Rdv` : `id` signifie exclusivement l'ID technique; `numeroRdv` reste un numéro métier.
- Supprimer les fallbacks ambigus `rdv.idRdv || rdv.id`.
- Remplacer les chaînes frontend delete/recreate par des commandes backend transactionnelles.
- Masquer BIC/IBAN dans les erreurs et logs.

### Phase 4 — contraction différée

Après au moins un déploiement compatible validé :

- retirer les anciennes routes;
- retirer `EtudeVolontaireId`, `RdvId`, `InfobancaireId`, `PreetudevolId`;
- supprimer les allocateurs uniquement si le numéro RDV métier est abandonné;
- supprimer les anciennes colonnes d'identité seulement si elles ne sont plus des données métier;
- passer Hibernate à `ddl-auto=validate`.

## Tests obligatoires

- migration SQL sur une restauration de la copie locale;
- génération des quatre IDs techniques;
- update conservant le même ID;
- rejet des doublons métier concurrents;
- attribution concurrente des numéros sujet;
- conflits payé/non payé et montants divergents;
- paiement en masse atomique;
- annulation pointant vers l'ancien RDV après remplacement;
- routes anciennes et nouvelles pendant la compatibilité;
- absence d'IBAN/BIC dans erreurs et logs;
- rollback applicatif et restauration du dump.

## Rollback

### Avant contraction

Redéployer l'ancien backend. Les colonnes historiques et leurs contraintes uniques restent disponibles; les nouveaux IDs peuvent être ignorés.

### Après canonicalisation

Restaurer les lignes depuis les tables d'archive, après retrait temporaire des nouvelles contraintes. Comparer les comptes et checksums.

### Après contraction irréversible

Restaurer le dump vérifié, puis redéployer l'ancienne version. Aucun script inverse ne remplace une sauvegarde réellement restaurable.

## Blocage actuel avant DDL

L'environnement d'agent ne possède ni client `mysql`, ni `DB_URL`, ni `DB_PASSWORD`. Le schéma MySQL réel n'a donc pas encore été interrogé. Aucun script `ALTER TABLE` définitif ne doit être écrit ou exécuté avant récupération des résultats du préflight.
