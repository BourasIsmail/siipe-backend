package ma.entraide.siipe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "programme")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Programme extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomFr;

    @Column(nullable = false)
    private String nomAr;

    @Column
    private String description;

    @OneToMany(mappedBy = "programme", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prestation> prestations;
}
