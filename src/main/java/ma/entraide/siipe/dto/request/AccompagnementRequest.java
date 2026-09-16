package ma.entraide.siipe.dto.request;

import lombok.Data;

@Data
public class AccompagnementRequest {
    private String preciserAccompagnement;
    private String nomAccompagnement;
    private String prenomAccompagnement;
    private String adresseAccompagnement;
    private String telephoneAccompagnement;
    private String emailAccompagnement;
}