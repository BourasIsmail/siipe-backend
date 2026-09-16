package ma.entraide.siipe.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ma.entraide.siipe.enums.TypePartenaire;

@Data
public class PartenaireRequest {
    @NotBlank private String nomFr;
    @NotBlank private String nomAr;
    private TypePartenaire type;
    private String telephone;
    private String email;
    private String adresse;
    private String responsable;
    private String description;
    private Long provinceId;
    private Long regionId;
}
