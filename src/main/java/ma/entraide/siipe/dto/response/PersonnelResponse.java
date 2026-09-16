package ma.entraide.siipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.entraide.siipe.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PersonnelResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String cin;
    private Sexe sexe;
    private String matricule;
    private String numCouvertureSociale;
    private String email;
    private String telephone;
    private LocalDate dateNaissance;
    private String lieuNaissance;
    private SituationFamille situationFamille;
    private Integer nombreEnfant;
    private String situationAdministratif;
    private LocalDate dateRecrutement;
    private Grade grade;
    private NiveauScolaire niveauScolaire;
    private Diplome diplome;
    private Double salaire;
    private String categorie;
    private Fonction fonction;
    private PosteOccupe posteOccupe;
    private String roleDescription;
    private String photoUrl;
    private Long etablissementCentreId;
    private String etablissementCentreNom;
    private Long programmeId;
    private String programmeNom;
    private Long prestationId;
    private String prestationNom;
    private Long provinceId;
    private String provinceNom;

    // Evaluation scores
    private Integer organisation;
    private Integer activite;
    private Integer specialisation;
    private Integer initiative;
    private Integer autonomie;
    private Integer adaptationProfessionnelle;
    private Integer relationsTravail;
    private Integer techniqueExecution;
    private Integer communication;
    private Integer toleranceStress;
    private Integer assiduitePointage;
    private Integer servicePopulation;

    // Observations
    private String observation1;
    private String observation2;
    private String observation3;
    private String observation4;
    private String observation5;
    private String observation6;
    private String observation7;
    private String observation8;
    private String observation9;
    private String observation10;
    private String observation11;
    private String observation12;
    private String observation13;
    private String observation14;
    private String observation15;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
