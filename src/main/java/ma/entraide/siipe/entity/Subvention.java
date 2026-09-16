package ma.entraide.siipe.entity;

import jakarta.persistence.*;
import lombok.*;
import ma.entraide.siipe.enums.StatutSubvention;

import java.time.LocalDate;

@Entity
@Table(name = "subvention")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Subvention extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column
    private String description;

    @Column(nullable = false)
    private Double montant;

    @Column
    private LocalDate dateDebut;

    @Column
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    private StatutSubvention statut;

    @Column
    private String conventionUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partenaire_id")
    private Partenaire partenaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etablissement_id")
    private EtablissementCentre etablissement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programme_id")
    private Programme programme;
}
