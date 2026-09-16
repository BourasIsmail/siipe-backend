package ma.entraide.siipe.entity;

import jakarta.persistence.*;
import lombok.*;
import ma.entraide.siipe.enums.TypePartenaire;

@Entity
@Table(name = "partenaire")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Partenaire extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomFr;

    @Column(nullable = false)
    private String nomAr;

    @Enumerated(EnumType.STRING)
    private TypePartenaire type;

    @Column
    private String telephone;

    @Column
    private String email;

    @Column
    private String adresse;

    @Column
    private String responsable;

    @Column
    private String description;

    @Column
    private String logoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id")
    private Province province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;
}
