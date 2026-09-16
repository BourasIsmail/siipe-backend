package ma.entraide.siipe.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SituationSocialeRequest {
    private String situationDifficulte;
    private String typeViolence;
    private String degre;
    private String sourceViolence;
    private String lieuViolence;
    private LocalDate dateViolence;
    private String violenceRepetee;
    private String observation;
}