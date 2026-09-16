package ma.entraide.siipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DossierScolaireResponse {
    private Long id;
    private String type;
    private LocalDate date;
    private String decisions;
    private String pieceJointeUrl;
}