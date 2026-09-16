package ma.entraide.siipe.entity;

import jakarta.persistence.*;
import lombok.*;
import ma.entraide.siipe.enums.Fonction;
import ma.entraide.siipe.enums.NiveauScolaire;

@Entity
@Table(name = "poste_profil")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PosteProfil extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String intitule;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private Fonction fonction;

    @Enumerated(EnumType.STRING)
    private NiveauScolaire niveauScolaireRequis;

    @Column
    private String competencesRequises;

    @Column
    private Integer nombrePostes;

    @Column
    private Integer nombrePourvu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etablissement_centre_id")
    private EtablissementCentre etablissementCentre;
}
