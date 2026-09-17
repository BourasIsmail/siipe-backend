package ma.entraide.siipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.entraide.siipe.enums.Sexe;
import ma.entraide.siipe.enums.SituationDifficulte;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BeneficiaireResponse {
    private Long id;
    private String nom;
    private String prenom;
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
    private String photoUrl;
    private Long etablissementCentreId;
    private String etablissementCentreNom;
    private Long programmeId;
    private String programmeNom;
    private Long prestationId;
    private String prestationNom;
    private String assistanteSocialeNom;
    private Long provinceId;
    private String provinceNom;
    private Long regionId;
    private String regionNom;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}