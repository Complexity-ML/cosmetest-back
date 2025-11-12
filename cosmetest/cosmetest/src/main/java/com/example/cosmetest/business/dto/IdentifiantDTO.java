package com.example.cosmetest.business.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO (Data Transfer Object) pour l'entité Identifiant
 */
public class IdentifiantDTO {

    private Integer idIdentifiant;

    @NotBlank(message = "L'identifiant ne peut pas être vide")
    @Size(min = 3, max = 50, message = "L'identifiant doit contenir entre 3 et 50 caractères")
    private String identifiant;

    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String mdpIdentifiant;

    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "L'email doit être valide")
    private String mailIdentifiant;

    @NotBlank(message = "Le rôle ne peut pas être vide")
    @Pattern(regexp = "^(ADMIN|UTILISATEUR|MODERATEUR)$", message = "Le rôle doit être ADMIN, UTILISATEUR ou MODERATEUR")
    private String role;

    // Description optionnelle
    private String description;

    // Constructeurs
    public IdentifiantDTO() {
    }

    public IdentifiantDTO(Integer idIdentifiant, String identifiant, String mdpIdentifiant,
                          String mailIdentifiant, String role, String description) {
        this.idIdentifiant = idIdentifiant;
        this.identifiant = identifiant;
        this.mdpIdentifiant = mdpIdentifiant;
        this.mailIdentifiant = mailIdentifiant;
        this.description = description;
        this.role = role;
    }

    // Constructeur pour créer un DTO sans mot de passe (pour les réponses)
    public static IdentifiantDTO withoutPassword(Integer idIdentifiant, String identifiant,
                                                 String mailIdentifiant, String role, String description) {
        IdentifiantDTO dto = new IdentifiantDTO();
        dto.idIdentifiant = idIdentifiant;
        dto.identifiant = identifiant;
        dto.mailIdentifiant = mailIdentifiant;
        dto.role = role;
        dto.description = description;
        return dto;
    }

    // Getters et Setters
    public Integer getIdIdentifiant() {
        return idIdentifiant;
    }

    public void setIdIdentifiant(Integer idIdentifiant) {
        this.idIdentifiant = idIdentifiant;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getMdpIdentifiant() {
        return mdpIdentifiant;
    }

    public void setMdpIdentifiant(String mdpIdentifiant) {
        this.mdpIdentifiant = mdpIdentifiant;
    }

    public String getMailIdentifiant() {
        return mailIdentifiant;
    }

    public void setMailIdentifiant(String mailIdentifiant) {
        this.mailIdentifiant = mailIdentifiant;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Crée une version DTO sans le mot de passe pour des raisons de sécurité
     * @return un DTO sans le mot de passe
     */
    public IdentifiantDTO withoutPassword() {
        return withoutPassword(this.idIdentifiant, this.identifiant, this.mailIdentifiant, this.role, this.description);
    }
}