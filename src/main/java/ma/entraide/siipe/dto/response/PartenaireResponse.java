package ma.entraide.siipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.entraide.siipe.enums.TypePartenaire;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PartenaireResponse {
    private Long id;
    private String nomFr;
    private String nomAr;
    private TypePartenaire type;
    private String telephone;
    private String email;
    private String adresse;
    private String responsable;
    private String description;
    private String logoUrl;
    private Long provinceId;
    private String provinceNom;
    private Long regionId;
    private String regionNom;
    private LocalDateTime createdAt;
    private String createdBy;
}
