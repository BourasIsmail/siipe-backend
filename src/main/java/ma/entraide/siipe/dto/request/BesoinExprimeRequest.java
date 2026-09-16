package ma.entraide.siipe.dto.request;

import lombok.Data;

@Data
public class BesoinExprimeRequest {
    private String libelle;
    private Long programmeId;
    private Long prestationId;
    private String description;
}