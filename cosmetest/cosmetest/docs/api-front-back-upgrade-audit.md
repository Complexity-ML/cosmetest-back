# Audit d’upgrade API front/back

Date : 2026-07-13  
Périmètre de ce lot : client HTTP frontend, contrats authentification/identifiants/paiements, gestion des erreurs, CORS, autorisations et contrôleurs administratifs. Les tests API réels avec serveur et base sont volontairement différés.

## Critique — corrigé

- [x] **Ne plus traiter un `403` comme une session expirée.** Le client redirige vers `/login` uniquement sur `401`; un refus d’autorisation `403` reste visible sans déconnecter l’utilisateur.
- [x] **Ne plus journaliser les corps de requête/réponse Axios.** L’intercepteur n’écrit que des métadonnées allowlistées (`url`, `status`, `code`, `method`).
- [x] **Supprimer le second client Axios divergent.** `apiService.ts` réexporte le client canonique configuré avec cookies HttpOnly et politique `401` commune.
- [x] **Aligner le changement de mot de passe.** Le frontend utilise maintenant `POST /identifiants/{id}/changer-mot-de-passe` avec `ancienMotDePasse` et `nouveauMotDePasse`.
- [x] **Aligner l’action “tout payer”.** Le frontend utilise `POST /paiements/etudes/{idEtude}/mark-all-paid`, réellement exposé par `PaiementController`.
- [x] **Rétablir la mise à jour individuelle des paiements.** Le backend expose `PATCH /etude-volontaires/update-paiement`, réservé aux administrateurs, résout la clé composite côté serveur et rejette les associations absentes ou ambiguës.
- [x] **Empêcher les fuites d’erreurs de service** sur les lectures/paiements étude-volontaire; les exceptions techniques passent par `GlobalExceptionHandler`.
- [x] **Réparer les notifications des nouveaux volontaires.** Le frontend ne lit plus les cookies HttpOnly, ne télécharge plus tous les volontaires et distingue lecture, dismiss individuel et état par utilisateur/date. Le backend expose une projection minimale bornée de la journée.

## Élevé — corrigé

- [x] Centraliser les routes critiques dans `apiEndpoints.ts` pour empêcher les dérives `/api/api` et les divergences de méthode HTTP.
- [x] Protéger `/api/etude-volontaires/repair/**` par le rôle `ADMIN` dans la chaîne Spring Security.
- [x] Supprimer les `@CrossOrigin` locaux divergents des contrôleurs audit, connexions et étude-volontaire; la politique CORS globale est l’unique source.
- [x] Borner les paginations audit/connexions entre 1 et 100 éléments et normaliser les pages négatives à 0.
- [x] Uniformiser les erreurs méthode `401/403` avec `error`, `message`, `status`, `timestamp` et `path`.
- [x] Conserver des erreurs `400/404/500` structurées sans détails SQL/JDBC internes.

## Élevé — restant pour les lots suivants

- [ ] Remplacer les derniers `catch (Exception)` et usages de `e.getMessage()` dans les autres contrôleurs par le gestionnaire global, contrôleur par contrôleur avec tests de contrat.
- [ ] Traiter explicitement la protection CSRF pour l’authentification par cookie avant de modifier `csrf.disable()`; ce changement nécessite les tests API navigateur réels prévus.
- [ ] Supprimer ou implémenter les méthodes frontend de paiement encore sans route backend active (`batch`, statistiques, rapport, montants, permissions) après confirmation des usages métier.
- [ ] Centraliser les erreurs frontend de tous les services dans un type `ApiClientError`; plusieurs services historiques journalisent encore l’objet Axios brut.
- [ ] Ajouter une spécification OpenAPI et un test automatique de dérive entre routes Spring et endpoints frontend.

## Moyen — restant

- [ ] Harmoniser les enveloppes de succès (`ApiResponse`, objets directs et pages Spring) sans casser les écrans existants.
- [x] Réduire le chunk frontend principal sous le seuil Vite, charger les routes/onglets à la demande et différer `xlsx` jusqu'au clic d'export.
- [ ] Déprécier les anciennes routes composites après migration complète des consommateurs.

## Vérifications automatisées

- Frontend : tests Vitest complets, ESLint strict et build Vite.
- Backend : tests MVC/contrat ciblés sur sécurité, erreurs, paiements, audit et connexions.
- Suite backend globale : 499 tests exécutés; 498 passent. Le seul échec est `PhotoProxyServiceTest.rejectsTraversalAndAbsoluteUrlsBeforeAnyNetworkCall`, hors périmètre de ce lot et antérieur aux changements API.

## Tests API concrets différés

À exécuter avec le serveur lancé par l’utilisateur : login cookie, expiration `401`, refus `403` sans déconnexion, changement de mot de passe propriétaire/admin, paiement individuel, paiement groupé, pagination extrême, CORS depuis chaque origine autorisée et absence de détails techniques dans les réponses `500`.
