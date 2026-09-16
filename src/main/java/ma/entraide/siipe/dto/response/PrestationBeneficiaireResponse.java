package ma.entraide.siipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PrestationBeneficiaireResponse {
    private Long id;
    private Long etablissementId;
    private String etablissementNom;
    private Long programmeId;
    private String programmeNom;
    private Long prestationId;
    private String prestationNom;
    private String orientation;
    private String serviceInterne;
    private LocalDate dateDebut;
    private String statutPrestation;
    private String descriptionPhysique;
    private String pieceJointeUrl;
}