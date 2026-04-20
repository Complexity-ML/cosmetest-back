# Cosmetest - Application de Gestion d'Études Cosmétiques

## 📋 Description

Cosmetest est une application Spring Boot conçue pour la gestion d'études cosmétiques et de panels de volontaires. L'application permet de gérer les participants, organiser des rendez-vous, suivre les études en cours et administrer les panels de test.

## 🚀 Fonctionnalités Principales

### Gestion des Volontaires
- ✅ Inscription et gestion des profils volontaires
- ✅ Gestion des informations bancaires
- ✅ Suivi des activités récentes
- ✅ Historique des participations

### Gestion des Études
- ✅ Création et suivi des études cosmétiques
- ✅ Association volontaires/études
- ✅ Gestion des groupes d'étude
- ✅ Statistiques et rapports

### Système de Rendez-vous
- ✅ Planification des rendez-vous
- ✅ Gestion des annulations
- ✅ Suivi du statut des RDV
- ✅ Notifications

### Panel et HC (Hors Critères)
- ✅ Gestion des panels de volontaires
- ✅ Suivi des volontaires hors critères
- ✅ Statistiques des panels

### Dashboard et Reporting
- ✅ Tableau de bord avec statistiques
- ✅ Activités récentes
- ✅ Métriques quotidiennes
- ✅ Rapports détaillés

## 🛠️ Technologies Utilisées

- **Framework**: Spring Boot 3.x
- **Base de données**: JPA/Hibernate
- **Sécurité**: Spring Security + JWT
- **Build**: Gradle
- **Documentation API**: OpenAPI/Swagger
- **Tests**: JUnit

## 📁 Architecture du Projet

```
src/main/java/com/example/cosmetest/
├── business/
│   ├── dto/           # Data Transfer Objects
│   ├── mapper/        # Mappers entre entités et DTOs
│   ├── model/         # Modèles métier
│   └── service/       # Services métier
├── config/            # Configuration Spring
├── data/repository/   # Repositories JPA
├── domain/model/      # Entités de domaine
├── exception/         # Gestion des exceptions
├── filter/           # Filtres HTTP
├── presentation/
│   ├── controller/   # Contrôleurs REST
│   ├── request/      # DTOs de requête
│   └── response/     # DTOs de réponse
├── search/           # Services de recherche
├── security/         # Configuration sécurité JWT
└── utils/            # Utilitaires
```

## 🔧 Installation et Configuration

### Prérequis

- Java 17 ou supérieur
- Gradle 8.x
- Base de données (PostgreSQL/MySQL/H2)

### Étapes d'installation

1. **Cloner le repository**
```bash
git clone <url-du-repository>
cd cosmetest
```

2. **Configuration de la base de données**
Modifiez le fichier `src/main/resources/application.properties` :
```properties
# Configuration base de données
spring.datasource.url=jdbc:postgresql://localhost:5432/cosmetest
spring.datasource.username=your_username
spring.datasource.password=your_password

# Configuration JWT
jwt.secret=your-secret-key
jwt.expiration=86400

```

3. **Build et lancement**
```bash
# Build du projet
./gradlew build

# Lancement de l'application
./gradlew bootRun
```

L'application sera accessible sur `http://localhost:8080`

## 📚 API Documentation

Une fois l'application lancée, la documentation Swagger est disponible à :
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **Redoc**: `http://localhost:8080/redoc`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## 🔐 Authentification

L'application utilise JWT pour l'authentification. 

### Connexion
```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "your_username",
  "password": "your_password"
}
```

### Utilisation du token
```bash
Authorization: Bearer <your-jwt-token>
```

## 📊 Principales API Endpoints

### Volontaires
- `GET /api/volontaires` - Liste des volontaires
- `POST /api/volontaires` - Créer un volontaire
- `GET /api/volontaires/{id}` - Détails d'un volontaire
- `PUT /api/volontaires/{id}` - Modifier un volontaire

### Études
- `GET /api/etudes` - Liste des études
- `POST /api/etudes` - Créer une étude
- `GET /api/etudes/{id}/volontaires` - Volontaires d'une étude

### Rendez-vous
- `GET /api/rdv` - Liste des RDV
- `POST /api/rdv/planifier` - Planifier un RDV
- `PUT /api/rdv/{id}/annuler` - Annuler un RDV

### Dashboard
- `GET /api/dashboard/stats` - Statistiques générales
- `GET /api/dashboard/activites-recentes` - Activités récentes

## 🧪 Tests

```bash
# Exécuter tous les tests
./gradlew test

# Générer le rapport de couverture
./gradlew jacocoTestReport
```

## 📝 Logs

Les logs sont configurés pour être sauvegardés dans le répertoire `logs/`:
- `user-actions.log` - Actions des utilisateurs
- Rotation automatique des logs par jour

## 🔍 Recherche


## 🚀 Déploiement

### Profile de production
```bash
./gradlew bootJar
java -jar -Dspring.profiles.active=prod build/libs/cosmetest-0.0.1-SNAPSHOT.jar
```

### Docker (optionnel)
```dockerfile
FROM openjdk:17-jdk-slim
COPY build/libs/cosmetest-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🤝 Contribution

1. Fork le projet
2. Créer une branche (`git checkout -b feature/nouvelle-fonctionnalite`)
3. Commit les modifications (`git commit -am 'Ajout nouvelle fonctionnalité'`)
4. Push vers la branche (`git push origin feature/nouvelle-fonctionnalite`)
5. Créer une Pull Request

## 📄 Licence

Ce projet est sous licence [MIT](LICENSE).

## 👥 Équipe

- **Développement**: [Votre nom]
- **Contact**: [votre.email@example.com]

## 📞 Support

Pour toute question ou problème :
- 📧 Email: support@cosmetest.com
- 🐛 Issues: [GitHub Issues](lien-vers-issues)
- 📖 Documentation: [Wiki du projet](lien-vers-wiki)

---

**Version**: 0.0.1-SNAPSHOT  
**Dernière mise à jour**: Juillet 2025
