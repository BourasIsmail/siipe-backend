package ma.entraide.siipe.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SituationMedicaleRequest {
    private String situationMedicale;
    private String historiqueMedicale;
    private String medecin;
    private String utilisationMedicament;
    private LocalDate date;
    private String observation;
}