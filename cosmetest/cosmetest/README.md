# Cosmetest — backend

API Spring Boot de l'application Cosmetest. Elle porte les règles métier et la persistance liées aux volontaires, études, groupes, rendez-vous, annulations, indemnités, paiements, rapports, comptes et journaux d'audit.

Le frontend associé se trouve dans le dépôt [`Complexity-ML/cosmetest-front`](https://github.com/Complexity-ML/cosmetest-front).

## Stack technique

| Composant | Version ou implémentation |
|---|---|
| Java | 21 |
| Spring Boot | 3.5.16 |
| Gradle Wrapper | 8.11.1 |
| API HTTP | Spring MVC |
| Persistance | Spring Data JPA / Hibernate |
| Base de données | MySQL via `mysql-connector-j` |
| Sécurité | Spring Security, JWT, cookie HttpOnly |
| Documentation | SpringDoc OpenAPI 2.8.17 |
| Tests | JUnit 5, Spring Boot Test, Spring Security Test, H2, MockWebServer |

## Prérequis

- JDK 21 ;
- une instance MySQL accessible ;
- Git ;
- aucun Gradle global n'est nécessaire : le wrapper est versionné.

En production, la base Cosmetest est fournie par MySQL sous WampServer. Le backend est exécuté comme service Windows via WinSW ; la configuration WinSW reste extérieure à ce dépôt.

## Installation

```bash
git clone https://github.com/Complexity-ML/cosmetest-back.git
cd cosmetest-back/cosmetest/cosmetest
cp example.env .env
```

Adaptez ensuite `.env` à votre environnement. Ce fichier est obligatoire pour les tâches qui traitent les ressources, notamment `bootRun`, `test`, `build` et `bootJar`.

Exemple sans valeur réelle :

```dotenv
DB_URL=[REDACTED]
DB_USERNAME=[REDACTED]
DB_PASSWORD=[REDACTED]
JWT_SECRET=[REDACTED]
PHOTO_SERVER_URL=[REDACTED]
```

Ne commitez jamais `.env`. Les noms reconnus par la configuration applicative sont :

| Variable | Obligatoire | Rôle |
|---|---:|---|
| `DB_URL` | recommandée | URL JDBC MySQL |
| `DB_USERNAME` | recommandée | utilisateur MySQL |
| `DB_PASSWORD` | oui | mot de passe MySQL |
| `JWT_SECRET` | oui | signature des JWT |
| `PHOTO_SERVER_URL` | non | origine du serveur de photos |
| `PHOTO_MAX_RESPONSE_SIZE` | non | taille maximale acceptée par le proxy photo |
| `SPRING_PROFILES_ACTIVE` | non | profil Spring ; `bootRun` utilise `local` par défaut |
| `HIBERNATE_DDL_AUTO` | non | stratégie Hibernate ; conserver `none` sur le serveur |
| `SWAGGER_ENABLED` | non | active l'interface Swagger UI |
| `SERVER_ERROR_INCLUDE_STACKTRACE` | non | exposition des traces dans les réponses d'erreur |
| `RUN_LIVE_DB_AUDIT` | non | active le test facultatif d'audit en lecture seule de la base réelle |

> **Important — secrets embarqués :** le build actuel copie `.env` dans le JAR sous le nom `application-packaged-secrets.properties`. Le JAR doit donc être traité comme un artefact sensible : accès restreint, aucun dépôt Git, aucune diffusion publique. Un changement de secret impose actuellement un nouveau build. Cette contrainte est connue et devra être remplacée à terme par une injection de secrets au démarrage.

## Développement local

Le backend écoute sur le port `8888`. Pour un développement HTTP cohérent avec les cookies, utilisez explicitement le profil `dev` :

```bash
SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun --no-daemon --console=plain
```

Sous `cmd.exe` :

```bat
set SPRING_PROFILES_ACTIVE=dev
gradlew.bat bootRun --no-daemon --console=plain
```

Sans variable explicite, `bootRun` choisit actuellement `local`. Or le code des cookies considère que seul le profil nommé exactement `dev` est non-production ; `local` peut donc produire un cookie `Secure` incompatible avec un accès HTTP local. Aucun fichier `application-local.properties` ou `application-prod.properties` n'est versionné.

Points de contrôle :

```text
API       http://127.0.0.1:8888
Santé     http://127.0.0.1:8888/api/health
OpenAPI   http://127.0.0.1:8888/v3/api-docs
```

L'interface Swagger est désactivée par défaut. Pour l'activer dans un environnement maîtrisé, définissez `SWAGGER_ENABLED=true`, puis ouvrez :

```text
http://127.0.0.1:8888/swagger-ui.html
```

## Authentification et autorisations

La connexion s'effectue avec :

```http
POST /api/auth/login
Content-Type: application/json

{
  "login": "...",
  "motDePasse": "..."
}
```

Le backend peut transmettre le JWT de trois manières pour assurer la compatibilité des clients existants :

- cookie `jwt` HttpOnly ;
- en-tête de réponse `Authorization: Bearer [REDACTED]` ;
- corps JSON de la réponse de connexion.

Le frontend web utilise le cookie avec `withCredentials: true`. Les endpoints utiles au cycle de session sont :

```text
POST /api/auth/login
GET  /api/auth/validate
GET  /api/auth/user
GET  /api/users/me
POST /api/auth/logout
```

Politique d'accès actuelle :

- `/api/health`, la connexion/déconnexion et la documentation OpenAPI sont publiques ;
- toutes les fonctions métier nécessitent un utilisateur authentifié ;
- les utilisateurs authentifiés ont accès aux fonctionnalités métier ;
- la création de compte (`POST /api/identifiants`) est réservée au rôle `ADMIN` ;
- les journaux d'audit et de connexion/session (`/api/audit/**`, `/api/connexions/**`) sont réservés au rôle `ADMIN`.

Les mots de passe historiques restent lisibles via un encodeur de compatibilité. Les nouveaux mots de passe sont enregistrés en BCrypt avec le préfixe Spring Security correspondant.

## Base de données

La configuration principale est dans `src/main/resources/application.properties`.

```properties
spring.jpa.hibernate.ddl-auto=none
spring.flyway.enabled=false
```

Conséquences :

- Hibernate ne crée et ne modifie pas le schéma de production ;
- aucune migration Flyway n'est exécutée ;
- toute évolution de schéma doit être préparée, sauvegardée, exécutée explicitement et validée avant le démarrage du JAR qui en dépend ;
- ne passez jamais `HIBERNATE_DDL_AUTO` à `update`, `create` ou `create-drop` sur le serveur.

Des scripts SQL existent sous `src/main/resources/db/migration/` et `docs/sql/`, mais leur présence ne signifie pas qu'ils sont exécutés automatiquement.

Deux `CommandLineRunner` normalisent actuellement des données à chaque démarrage :

- les numéros de téléphone ;
- les phototypes.

Un démarrage n'est donc pas strictement en lecture seule. Contrôlez les logs et utilisez une copie locale récente avant toute validation d'impact.

Pour le développement et les tests d'impact, utilisez une copie locale récente de la base plutôt que la base de production active.

## Tests et build

Suite complète :

```bash
./gradlew clean test --no-daemon --console=plain
```

Contrôle Git des espaces :

```bash
git diff --check
```

Création du JAR exécutable :

```bash
./gradlew bootJar --no-daemon --console=plain
```

Artefact à déployer :

```text
build/libs/cosmetest-0.0.1-SNAPSHOT.jar
```

N'utilisez pas le fichier `*-plain.jar` comme service Spring Boot.

## Architecture

```text
src/main/java/com/example/cosmetest/
├── business/                 DTO, mappers et services métier
├── config/                   sécurité, OpenAPI et configuration Spring
├── data/repository/          repositories Spring Data JPA
├── domain/model/             entités persistées
├── exception/                exceptions et traitement HTTP global
├── presentation/controller/  API REST
├── presentation/request/     contrats d'entrée
├── presentation/response/    contrats de sortie
├── security/                 filtres et outils JWT
└── utils/                    utilitaires partagés
```

Plusieurs domaines exposent une route historique et une route `/api/v1` : volontaires, études, associations étude-volontaire, rendez-vous et volontaires hors critères. Les routes historiques portent des en-têtes de dépréciation avec une cible de retrait au 31 janvier 2027. Privilégiez les routes `/api/v1` pour les nouveaux clients, sans supposer que toute l'API est déjà versionnée.

Les logs applicatifs sont écrits dans :

```text
logs/user-actions.log
```

## Déploiement Windows

La production connue utilise :

- MySQL sous WampServer ;
- un JAR Spring Boot ;
- WinSW comme gestionnaire du service Java ;
- le port backend `8888`.

Procédure recommandée :

1. exécuter la suite de tests et `bootJar` sur une machine de build maîtrisée ;
2. transférer le nouveau JAR dans un répertoire temporaire du serveur ;
3. sauvegarder le JAR actuellement en service et la configuration WinSW ;
4. si une évolution SQL est nécessaire, sauvegarder la base et préparer son rollback ;
5. arrêter proprement le service WinSW ;
6. appliquer l'évolution SQL validée, le cas échéant ;
7. remplacer le JAR sans modifier les variables ni les arguments du service ;
8. redémarrer WinSW ;
9. contrôler les logs et `GET /api/health` ;
10. effectuer une recette ciblée avant de supprimer la sauvegarde.

En cas d'échec au démarrage, arrêtez le service, restaurez le JAR précédent et redémarrez-le. Une évolution SQL non rétrocompatible doit disposer de sa propre procédure de rollback.

## Docker et intégration continue

Ce dépôt ne contient actuellement ni `Dockerfile`, ni fichier Compose, ni workflow CI versionné. Docker n'est pas la procédure de production documentée : ne confondez pas une éventuelle expérimentation locale avec l'architecture WampServer/WinSW du serveur.

## Vérification après déploiement

Minimum opérationnel :

```text
GET  /api/health        → 200 et {"status":"UP"}
GET  /v3/api-docs       → 200
GET  /api/auth/validate → 401 sans session
```

Puis validez avec un compte de test : connexion, consultation des études, groupes, volontaires et rendez-vous, puis déconnexion.

## Règles de contribution

- ne jamais commiter `.env`, un dump SQL, un JAR contenant des secrets ou des données de production ;
- travailler sur une branche dédiée lorsque le changement n'est pas une correction directe validée ;
- ajouter un test de régression pour toute correction métier ;
- exécuter `git diff --check` et les tests pertinents avant le push ;
- ne pas laisser Hibernate modifier automatiquement le schéma serveur.