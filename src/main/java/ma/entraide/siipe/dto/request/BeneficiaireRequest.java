package ma.entraide.siipe.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ma.entraide.siipe.enums.Sexe;
import ma.entraide.siipe.enums.SituationDifficulte;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class BeneficiaireRequest {
    @NotBlank private String nom;
    @NotBlank private String prenom;
    private String nomAr;
    private String prenomAr;
    private String alias;
    private String cin;
    private String numActeNaissance;
    private String numPasseport;
    private String numeroDossier;
    private String typePieceIdentite;
    private Sexe sexe;
    private LocalDate dateNaissance;
    private String lieuNaissance;
    private String nationalite;
    private String adresse;
    private String telephone;
    private SituationDifficulte situationDifficulte;
    private LocalDate dateEntree;
    private LocalDate dateSortie;
    private String motifSortie;
    private Boolean visitesADomicile;
    private String situationScolaire;
    private String situationFamiliale;
    private String temoignageFamille;
    private String descriptionPhysique;
    private String etatSantePsychique;
    private String situationProfessionnelle;
    private String sourceRevenu;
    private String couvertureSociale;
    private String revenuMensuel;
    private String etatComportement;
    private String description;
    private String nomPere;
    private String nomMere;
    private String nomTuteur;
    private String telephoneParent;
    private String adresseParent;
    private Long etablissementCentreId;
    private Long programmeId;
    private Long prestationId;
    private UUID assistanteSocialeId;
}