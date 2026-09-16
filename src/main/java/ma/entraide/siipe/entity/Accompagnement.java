package ma.entraide.siipe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "accompagnement")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Accompagnement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiaire_id", nullable = false)
    private Beneficiaire beneficiaire;

    @Column private String preciserAccompagnement;
    @Column private String nomAccompagnement;
    @Column private String prenomAccompagnement;
    @Column private String adresseAccompagnement;
    @Column private String telephoneAccompagnement;
    @Column private String emailAccompagnement;
}