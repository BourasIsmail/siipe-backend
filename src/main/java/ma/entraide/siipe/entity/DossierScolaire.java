package ma.entraide.siipe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "dossier_scolaire")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DossierScolaire extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiaire_id", nullable = false)
    private Beneficiaire beneficiaire;

    @Column private String type;
    @Column private LocalDate date;
    @Column(columnDefinition = "TEXT") private String decisions;
    @Column private String pieceJointeUrl;
}