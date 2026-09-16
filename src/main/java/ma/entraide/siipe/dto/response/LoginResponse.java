package ma.entraide.siipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class LoginResponse {
    private String token;
    private String email;
    private String nom;
    private String prenom;
    private String role;
    private Long provinceId;
    private String provinceNom;
    private Long regionId;
    private String regionNom;
    private Long etablissementCentreId;
    private String etablissementCentreNom;
}
