package ma.entraide.siipe.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DossierScolaireRequest {
    private String type;
    private LocalDate date;
    private String decisions;
}