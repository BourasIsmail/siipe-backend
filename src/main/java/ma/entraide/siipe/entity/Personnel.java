package ma.entraide.siipe.entity;

import jakarta.persistence.*;
import lombok.*;
import ma.entraide.siipe.enums.*;

import java.time.LocalDate;

@Entity
@Table(name = "personnel")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Personnel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column
    private String cin;

    @Enumerated(EnumType.STRING)
    private Sexe sexe;

    @Column(nullable = false, unique = true)
    private String matricule;

    @Column
    private String numCouvertureSociale;

    @Column
    private String email;

    @Column
    private String telephone;

    @Column
    private LocalDate dateNaissance;

    @Column
    private String lieuNaissance;

    @Enumerated(EnumType.STRING)
    private SituationFamille situationFamille;

    @Column
    private Integer nombreEnfant;

    // Professional info
    @Column
    private String situationAdministratif; // EN, Détachement, etc.

    @Column
    private LocalDate dateRecrutement;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Enumerated(EnumType.STRING)
    private NiveauScolaire niveauScolaire;

    @Enumerated(EnumType.STRING)
    private Diplome diplome;

    @Column
    private Double salaire;

    @Column
    private String categorie;

    @Enumerated(EnumType.STRING)
    private Fonction fonction;

    @Enumerated(EnumType.STRING)
    private PosteOccupe posteOccupe;

    @Column
    private String roleDescription; // role field from original (not Spring role)

    // Photo
    @Column
    private String photoUrl;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etablissement_centre_id")
    private EtablissementCentre etablissementCentre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programme_id")
    private Programme programme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestation_id")
    private Prestation prestation;

    // Evaluation scores (1-5)
    @Column private Integer organisation;
    @Column private Integer activite;
    @Column private Integer specialisation;
    @Column private Integer initiative;
    @Column private Integer autonomie;
    @Column private Integer adaptationProfessionnelle;
    @Column private Integer relationsTravail;
    @Column private Integer techniqueExecution;
    @Column private Integer communication;
    @Column private Integer toleranceStress;
    @Column private Integer assiduitePointage;
    @Column private Integer servicePopulation;

    // Observations (evaluations par période)
    @Column(columnDefinition = "TEXT") private String observation1;
    @Column(columnDefinition = "TEXT") private String observation2;
    @Column(columnDefinition = "TEXT") private String observation3;
    @Column(columnDefinition = "TEXT") private String observation4;
    @Column(columnDefinition = "TEXT") private String observation5;
    @Column(columnDefinition = "TEXT") private String observation6;
    @Column(columnDefinition = "TEXT") private String observation7;
    @Column(columnDefinition = "TEXT") private String observation8;
    @Column(columnDefinition = "TEXT") private String observation9;
    @Column(columnDefinition = "TEXT") private String observation10;
    @Column(columnDefinition = "TEXT") private String observation11;
    @Column(columnDefinition = "TEXT") private String observation12;
    @Column(columnDefinition = "TEXT") private String observation13;
    @Column(columnDefinition = "TEXT") private String observation14;
    @Column(columnDefinition = "TEXT") private String observation15;

    public String getFullName() {
        return nom + " " + prenom;
    }
}
