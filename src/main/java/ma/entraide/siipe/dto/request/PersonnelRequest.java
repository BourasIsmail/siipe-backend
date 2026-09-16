package ma.entraide.siipe.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ma.entraide.siipe.enums.*;

import java.time.LocalDate;

@Data
public class PersonnelRequest {
    @NotBlank private String nom;
    @NotBlank private String prenom;
    private String cin;
    private Sexe sexe;
    @NotBlank private String matricule;
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
    @NotNull private Long etablissementCentreId;
    private Long programmeId;
    private Long prestationId;

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
}
