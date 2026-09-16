package ma.entraide.siipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AccompagnementResponse {
    private Long id;
    private String preciserAccompagnement;
    private String nomAccompagnement;
    private String prenomAccompagnement;
    private String adresseAccompagnement;
    private String telephoneAccompagnement;
    private String emailAccompagnement;
}