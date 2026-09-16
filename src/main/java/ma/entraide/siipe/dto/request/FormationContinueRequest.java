package ma.entraide.siipe.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class FormationContinueRequest {
    @NotBlank private String titre;
    private String description;
    private String organisme;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String lieu;
    private Integer dureeJours;
    private Long etablissementCentreId;
    private List<Long> participantIds;
}