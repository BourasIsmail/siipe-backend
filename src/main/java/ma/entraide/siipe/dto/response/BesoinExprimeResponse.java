package ma.entraide.siipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BesoinExprimeResponse {
    private Long id;
    private String libelle;
    private Long programmeId;
    private String programmeNom;
    private Long prestationId;
    private String prestationNom;
    private String description;
}