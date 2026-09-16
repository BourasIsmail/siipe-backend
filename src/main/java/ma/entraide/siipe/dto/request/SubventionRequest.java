package ma.entraide.siipe.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ma.entraide.siipe.enums.StatutSubvention;

import java.time.LocalDate;

@Data
public class SubventionRequest {
    @NotBlank private String titre;
    private String description;
    @NotNull private Double montant;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private StatutSubvention statut;
    private Long partenaireId;
    private Long etablissementId;
    private Long programmeId;
}
