package ma.entraide.siipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.entraide.siipe.enums.Fonction;
import ma.entraide.siipe.enums.NiveauScolaire;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PosteProfilResponse {
    private Long id;
    private String intitule;
    private String description;
    private Fonction fonction;
    private NiveauScolaire niveauScolaireRequis;
    private String competencesRequises;
    private Integer nombrePostes;
    private Integer nombrePourvu;
    private Long etablissementCentreId;
    private String etablissementCentreNom;
}
