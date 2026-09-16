package ma.entraide.siipe.entity;

import jakarta.persistence.*;
import lombok.*;
import ma.entraide.siipe.enums.Sexe;
import ma.entraide.siipe.enums.SituationDifficulte;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "beneficiaire")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Beneficiaire extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identity
    @Column(nullable = false) private String nom;
    @Column(nullable = false) private String prenom;
    @Column private String nomAr;
    @Column private String prenomAr;
    @Column private String alias;
    @Column(unique = true) private String cin;
    @Column private String numActeNaissance;
    @Column private String numPasseport;
    @Column private String numeroDossier;
    @Column private String typePieceIdentite;

    @Enumerated(EnumType.STRING)
    private Sexe sexe;

    @Column private LocalDate dateNaissance;
    @Column private String lieuNaissance;
    @Column private String nationalite;
    @Column private String adresse;
    @Column private String telephone;

    // Situation
    @Enumerated(EnumType.STRING)
    private SituationDifficulte situationDifficulte;

    @Column private LocalDate dateEntree;
    @Column private LocalDate dateSortie;
    @Column private String motifSortie;
    @Column private Boolean visitesADomicile;

    // Social & Health info
    @Column private String situationScolaire;
    @Column private String situationFamiliale;
    @Column private String temoignageFamille;
    @Column(columnDefinition = "TEXT") private String descriptionPhysique;
    @Column private String etatSantePsychique;
    @Column private String situationProfessionnelle;
    @Column private String sourceRevenu;
    @Column private String couvertureSociale;
    @Column private String revenuMensuel;
    @Column private String etatComportement;
    @Column(columnDefinition = "TEXT") private String description;

    // Parents/Tuteurs
    @Column private String nomPere;
    @Column private String nomMere;
    @Column private String nomTuteur;
    @Column private String telephoneParent;
    @Column private String adresseParent;

    // Photo
    @Column private String photoUrl;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assistante_sociale_id")
    private User assistanteSociale;

    // Sub-sections (historical)
    @OneToMany(mappedBy = "beneficiaire", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SituationMedicale> situationsMedicales;

    @OneToMany(mappedBy = "beneficiaire", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SituationSociale> situationsSociales;

    @OneToMany(mappedBy = "beneficiaire", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SituationJudiciaire> situationsJudiciaires;

    @OneToMany(mappedBy = "beneficiaire", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DossierScolaire> dossiersScolaires;

    @OneToMany(mappedBy = "beneficiaire", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Accompagnement> accompagnements;

    @OneToMany(mappedBy = "beneficiaire", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BesoinExprime> besoinsExprimes;

    @OneToMany(mappedBy = "beneficiaire", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PrestationBeneficiaire> prestationsBeneficiaire;
}