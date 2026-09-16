package ma.entraide.siipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SituationJudiciaireResponse {
    private Long id;
    private String peine;
    private String peinesCriminelles;
    private String duree;
    private String lieux;
    private LocalDate date;
    private String pieceJointeUrl;
    private String description;
}