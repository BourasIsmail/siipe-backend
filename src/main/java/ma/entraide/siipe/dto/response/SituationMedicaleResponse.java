package ma.entraide.siipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SituationMedicaleResponse {
    private Long id;
    private String situationMedicale;
    private String historiqueMedicale;
    private String medecin;
    private String utilisationMedicament;
    private LocalDate date;
    private String certificatUrl;
    private String observation;
}