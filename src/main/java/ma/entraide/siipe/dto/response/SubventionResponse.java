package ma.entraide.siipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.entraide.siipe.enums.StatutSubvention;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SubventionResponse {
    private Long id;
    private String titre;
    private String description;
    private Double montant;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private StatutSubvention statut;
    private String conventionUrl;
    private Long partenaireId;
    private String partenaireNom;
    private Long etablissementId;
    private String etablissementNom;
    private Long programmeId;
    private String programmeNom;
    private LocalDateTime createdAt;
    private String createdBy;
}
