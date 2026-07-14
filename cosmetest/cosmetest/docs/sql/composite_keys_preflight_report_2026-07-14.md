# Rapport de préflight des clés composites — copie locale

Date : 2026-07-14  
Cible vérifiée : base locale `cosmetest_java`, MySQL 5.7.23, port 3306.  
Mode : lecture seule (`SET SESSION TRANSACTION READ ONLY`). Aucune donnée n'a été modifiée.

## Volumes

| Table | Lignes |
|---|---:|
| `etude_volontaire` | 57 327 |
| `rdv` | 139 577 |
| `infobancaire` | 5 179 |
| `preetudevol` | 172 |
| `annulation` | 4 692 |

## Schéma réel constaté

- `rdv` possède réellement une PK composite `(ID_RDV, ID_ETUDE)`.
- `etude_volontaire` ne possède aucune PK déclarée dans `information_schema`; trois FK existent vers étude, groupe et volontaire.
- `infobancaire` ne possède aucune PK/contrainte déclarée dans le résultat du préflight.
- `preetudevol` ne possède aucune PK/contrainte déclarée dans le résultat du préflight.
- `annulation` possède une PK simple.

Le mapping JPA `@EmbeddedId` ne reflète donc pas complètement les contraintes réellement présentes en base pour trois tables.

## `etude_volontaire`

| Contrôle | Résultat |
|---|---:|
| Couples `(étude, volontaire)` dupliqués | 48 |
| Lignes excédentaires | 48 |
| Groupes avec divergence de paiement | 0 |
| Groupes avec divergence de montant `IV` | 12 |
| Groupes avec divergence de numéro sujet | 46 |
| Groupes avec divergence de statut | 13 |
| Groupes avec divergence de groupe | 1 |
| Numéros sujet positifs dupliqués dans une étude | 103 |
| Volontaires `NULL/0` | 0 |
| Groupes `NULL/0` | 0 |
| Études orphelines | 0 |
| Volontaires orphelins | 0 |
| Groupe appartenant à une autre étude | 0 |

### Décision

La canonicalisation automatique est interdite pour les 48 couples tant qu'une règle métier n'a pas été fixée. Le paiement est cohérent, mais 12 montants, 46 numéros sujet, 13 statuts et un groupe divergent. Les lignes doivent être archivées avant résolution.

## `rdv` et `annulation`

| Contrôle | Résultat |
|---|---:|
| Doublons de la PK historique RDV | 0 |
| RDV orphelins étude/volontaire | 0 |
| Lignes `annulation` sans RDV correspondant au couple historique | 4 655 / 4 692 |

### Décision

L'ajout direct d'une FK `annulation.RDV_ID -> rdv.RDV_ID` est bloqué. La quasi-totalité des annulations référence un RDV historique qui n'existe plus sous le couple stocké. Il faut préserver ces lignes comme historique autonome et définir si `RDV_ID` reste nullable, si une table d'archive RDV est nécessaire ou si seules les 37 correspondances existantes sont rétroalimentées.

## `infobancaire`

| Contrôle | Résultat |
|---|---:|
| Triplets métier dupliqués | 3 |
| Lignes excédentaires | 37 |
| Références volontaire orphelines | 220 |

### Décision

Les doublons sont exacts sur le triplet métier et peuvent être archivés puis dédupliqués après validation. Les 220 références orphelines bloquent l'ajout immédiat d'une FK vers `volontaire` et nécessitent une décision de conservation/suppression métier. Aucun BIC ou IBAN n'est reproduit dans ce rapport.

## `preetudevol`

| Contrôle | Résultat |
|---|---:|
| Triplets métier dupliqués | 67 |
| Lignes excédentaires | 67 |
| Études orphelines | 136 |
| Groupes orphelins | 136 |
| Volontaires orphelins | 3 |
| Groupe rattaché à une autre étude | 0 |

### Décision

La table contient seulement 172 lignes, mais la majorité référence des études/groupes absents. Aucune FK ne doit être ajoutée avant qualification métier. Les doublons exacts peuvent être archivés/dédupliqués; les 136 lignes étude/groupe orphelines doivent être conservées en archive ou supprimées seulement après validation.

## Portes bloquantes avant migration DDL

1. Sauvegarde restaurable de la copie locale non encore réalisée dans ce lot.
2. Règle de canonicalisation des 48 participations conflictuelles non validée.
3. Règle pour les 103 numéros sujet positifs dupliqués non validée.
4. Politique historique pour les 4 655 annulations sans RDV courant non validée.
5. Politique de conservation des 220 informations bancaires orphelines non validée.
6. Politique de conservation des 136 pré-études orphelines non validée.

Aucun `ALTER TABLE`, `UPDATE`, `DELETE`, `INSERT`, `DROP` ou `TRUNCATE` n'a été exécuté.
