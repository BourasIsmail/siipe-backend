package ma.entraide.siipe.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PrestationBeneficiaireRequest {
    private Long etablissementId;
    private Long programmeId;
    private Long prestationId;
    private String orientation;
    private String serviceInterne;
    private LocalDate dateDebut;
    private String statutPrestation;
    private String descriptionPhysique;
}