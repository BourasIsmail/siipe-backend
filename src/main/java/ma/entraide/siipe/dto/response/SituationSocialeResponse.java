package ma.entraide.siipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SituationSocialeResponse {
    private Long id;
    private String situationDifficulte;
    private String typeViolence;
    private String degre;
    private String sourceViolence;
    private String lieuViolence;
    private LocalDate dateViolence;
    private String violenceRepetee;
    private String observation;
}