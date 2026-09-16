package ma.entraide.siipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PrestationResponse {
    private Long id;
    private String nomFr;
    private String nomAr;
    private String description;
    private Long programmeId;
    private String programmeNom;
}
