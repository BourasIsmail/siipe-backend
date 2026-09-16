package ma.entraide.siipe.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ma.entraide.siipe.enums.Fonction;
import ma.entraide.siipe.enums.NiveauScolaire;

@Data
public class PosteProfilRequest {
    @NotBlank private String intitule;
    private String description;
    private Fonction fonction;
    private NiveauScolaire niveauScolaireRequis;
    private String competencesRequises;
    private Integer nombrePostes;
    private Integer nombrePourvu;
    private Long etablissementCentreId;
}
