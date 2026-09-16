package ma.entraide.siipe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "prestation_beneficiaire")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrestationBeneficiaire extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiaire_id", nullable = false)
    private Beneficiaire beneficiaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etablissement_id")
    private EtablissementCentre etablissement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programme_id")
    private Programme programme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestation_id")
    private Prestation prestation;

    @Column private String orientation;
    @Column private String serviceInterne;
    @Column private LocalDate dateDebut;
    @Column private String statutPrestation;
    @Column(columnDefinition = "TEXT") private String descriptionPhysique;
    @Column private String pieceJointeUrl;
}