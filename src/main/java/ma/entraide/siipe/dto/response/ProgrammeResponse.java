package ma.entraide.siipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProgrammeResponse {
    private Long id;
    private String nomFr;
    private String nomAr;
    private String description;
    private List<PrestationResponse> prestations;
}
