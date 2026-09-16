package ma.entraide.siipe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "situation_judiciaire")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SituationJudiciaire extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiaire_id", nullable = false)
    private Beneficiaire beneficiaire;

    @Column private String peine;
    @Column private String peinesCriminelles;
    @Column private String duree;
    @Column private String lieux;
    @Column private LocalDate date;
    @Column private String pieceJointeUrl;
    @Column(columnDefinition = "TEXT") private String description;
}