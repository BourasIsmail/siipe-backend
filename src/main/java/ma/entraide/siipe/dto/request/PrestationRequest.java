package ma.entraide.siipe.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrestationRequest {
    @NotBlank private String nomFr;
    @NotBlank private String nomAr;
    private String description;
    @NotNull private Long programmeId;
}
