package ma.entraide.siipe.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProgrammeRequest {
    @NotBlank private String nomFr;
    @NotBlank private String nomAr;
    private String description;
}
