package ma.entraide.siipe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "situation_medicale")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SituationMedicale extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiaire_id", nullable = false)
    private Beneficiaire beneficiaire;

    @Column private String situationMedicale;
    @Column(columnDefinition = "TEXT") private String historiqueMedicale;
    @Column private String medecin;
    @Column private String utilisationMedicament;
    @Column private LocalDate date;
    @Column private String certificatUrl;
    @Column(columnDefinition = "TEXT") private String observation;
}