package ma.entraide.siipe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "situation_sociale")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SituationSociale extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiaire_id", nullable = false)
    private Beneficiaire beneficiaire;

    @Column private String situationDifficulte;
    @Column private String typeViolence;
    @Column private String degre;
    @Column private String sourceViolence;
    @Column private String lieuViolence;
    @Column private LocalDate dateViolence;
    @Column private String violenceRepetee;
    @Column(columnDefinition = "TEXT") private String observation;
}