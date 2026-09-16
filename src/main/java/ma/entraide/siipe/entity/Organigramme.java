package ma.entraide.siipe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "organigramme")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Organigramme extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column
    private String description;

    @Column
    private String fichierUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etablissement_centre_id")
    private EtablissementCentre etablissementCentre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id")
    private Personnel responsable;
}
