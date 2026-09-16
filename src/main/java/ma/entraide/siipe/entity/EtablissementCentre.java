package ma.entraide.siipe.entity;

import jakarta.persistence.*;
import lombok.*;
import ma.entraide.siipe.enums.GererPar;
import ma.entraide.siipe.enums.Milieu;
import ma.entraide.siipe.enums.ProprieteType;
import ma.entraide.siipe.enums.TypeLocal;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "etablissement_centre")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EtablissementCentre extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomFr;

    @Column(nullable = false)
    private String nomAr;

    @Column(unique = true)
    private String code;

    @Column private String telephone;
    @Column private String fax;
    @Column private String adresse;

    @Enumerated(EnumType.STRING)
    private Milieu milieu;

    @Enumerated(EnumType.STRING)
    private TypeLocal typeLocal;

    @Enumerated(EnumType.STRING)
    private ProprieteType proprietecentre;

    @Enumerated(EnumType.STRING)
    private GererPar gererPar;

    @Column private String personneResponsableCentre;

    // Geographic coordinates
    @Column private Double latitude;
    @Column private Double longitude;

    // Construction info
    @Column private LocalDate dateConstruction;
    @Column private LocalDate dateExploitation;
    @Column private LocalDate dateAchat;
    @Column private Double superficieTerrain;
    @Column private Double surfaceBatie;
    @Column private Double superficieTotaleEtages;
    @Column private Integer nombreEtage;
    @Column private String etagesUtilises;
    @Column private String composant;
    @Column private Integer capaciteAccueil;
    @Column private String etatConstruction;
    @Column(columnDefinition = "TEXT") private String observation;

    // Loyer & Foncier
    @Column private String numerotitre;
    @Column private Boolean loyer;
    @Column private Double montantLoyer;
    @Column private String paieLoyer;

    // Utilities
    @Column private Boolean raccordementEauPotable;
    @Column private Boolean raccordementElectricite;

    // Plans & Documents
    @Column private Boolean planSituation;
    @Column private Boolean planArchitecture;

    // Litige
    @Column private Boolean litige;
    @Column private String raisonsConflit;
    @Column private Double prixBatiment;

    // Authorization
    @Column private Boolean autorise;
    @Column private String numeroAutorisation;

    @Column private String utilisation;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id")
    private Province province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    // File uploads
    @Column private String mappeCadastraleUrl;
    @Column private String certificatProprieteUrl;
    @Column private String planSituationUrl;
    @Column private String planArchitectureUrl;
    @Column private String photoUrl;

    @ManyToMany
    @JoinTable(
            name = "etablissement_programme",
            joinColumns = @JoinColumn(name = "etablissement_id"),
            inverseJoinColumns = @JoinColumn(name = "programme_id")
    )
    private List<Programme> programmes;

    @ManyToMany
    @JoinTable(
            name = "etablissement_partenaire",
            joinColumns = @JoinColumn(name = "etablissement_id"),
            inverseJoinColumns = @JoinColumn(name = "partenaire_id")
    )
    private List<Partenaire> partenaires;

    @ManyToMany
    @JoinTable(
            name = "etablissement_prestation",
            joinColumns = @JoinColumn(name = "etablissement_id"),
            inverseJoinColumns = @JoinColumn(name = "prestation_id")
    )
    private List<Prestation> prestations;
}