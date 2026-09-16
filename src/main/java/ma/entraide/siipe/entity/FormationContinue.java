package ma.entraide.siipe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "formation_continue")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FormationContinue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column
    private String description;

    @Column
    private String organisme;

    @Column
    private LocalDate dateDebut;

    @Column
    private LocalDate dateFin;

    @Column
    private String lieu;

    @Column
    private Integer dureeJours;

    @Column
    private String attestationUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etablissement_centre_id")
    private EtablissementCentre etablissementCentre;

    @ManyToMany
    @JoinTable(
            name = "formation_personnel",
            joinColumns = @JoinColumn(name = "formation_id"),
            inverseJoinColumns = @JoinColumn(name = "personnel_id")
    )
    private List<Personnel> participants;
}
