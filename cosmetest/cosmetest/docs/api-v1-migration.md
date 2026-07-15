# Migration progressive de l’API vers `/api/v1`

## Objectif

Le backend expose progressivement des routes versionnées sans interrompre les clients historiques. Les routes existantes restent fonctionnelles pendant la période de migration.

## Routes actuellement doublées

| Domaine | Route historique | Route versionnée |
|---|---|---|
| Volontaires | `/api/volontaires/**` | `/api/v1/volontaires/**` |
| Études | `/api/etudes/**` | `/api/v1/etudes/**` |
| Associations étude-volontaire | `/api/etude-volontaires/**` | `/api/v1/etude-volontaires/**` |
| Rendez-vous | `/api/rdvs/**` | `/api/v1/rdvs/**` |
| Habitudes cosmétiques | `/api/volontaires-hc/**` | `/api/v1/volontaires-hc/**` |

Les routes qui ne figurent pas dans ce tableau ne doivent pas être supposées disponibles sous `/api/v1`.

## Signalement des routes historiques

Une réponse servie par une route historique doublée contient :

```http
Deprecation: true
Sunset: Sat, 31 Jan 2027 23:59:59 GMT
Link: </api/v1/...>; rel="successor-version"
```

La date `Sunset` est une cible de migration et doit être confirmée avant toute suppression effective. Aucune route historique ne doit être supprimée uniquement parce que cette date est présente : la suppression exige un audit des consommateurs, une annonce et un lot de déploiement distinct.

Les routes `/api/v1/**` ne reçoivent pas ces en-têtes.

## Compatibilité

- Les alias versionnés appellent les mêmes contrôleurs et services que les routes historiques.
- Les contrats JSON existants sont conservés lors de l’introduction d’un alias.
- Les nouvelles opérations peuvent utiliser des DTO dédiés et n’ont pas à reproduire les contrats génériques historiques.
- Les mutations modernes utilisent les identifiants techniques.
- Une route historique dont la clé métier correspond à plusieurs lignes répond `409 Conflict`; elle ne choisit ni ne supprime une ligne arbitrairement.

## Contrat d’erreur

Les nouveaux traitements utilisent le contrat structuré centralisé :

```json
{
  "code": "RESOURCE_CONFLICT",
  "status": 409,
  "message": "Conflit de ressource",
  "details": "...",
  "fieldErrors": {},
  "correlationId": "...",
  "timestamp": "...",
  "path": "/api/v1/..."
}
```

Principaux codes d’ambiguïté introduits :

- `AMBIGUOUS_VOLUNTEER`
- `AMBIGUOUS_VOLUNTEER_HABITS`
- `AMBIGUOUS_APPOINTMENT_TRACE`
- `AMBIGUOUS_REPAIR_GROUP`

## Procédure de migration d’un client

1. Inventorier les routes historiques réellement appelées.
2. Remplacer uniquement celles disposant d’un alias documenté.
3. Vérifier les réponses `409` et ne jamais les convertir en sélection du premier résultat.
4. Tester authentification, pagination, erreurs de validation et mutations par identifiant technique.
5. Observer l’absence d’appels historiques dans les journaux avant toute proposition de suppression.

## Règles de retrait

Une route historique ne peut être retirée que si :

- tous les clients connus utilisent `/api/v1` ;
- la route versionnée a un test contractuel ;
- les autorisations sont équivalentes ou renforcées de manière documentée ;
- une stratégie de rollback existe ;
- le retrait est publié dans un lot séparé du refactoring interne.
