package ma.entraide.siipe.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SituationJudiciaireRequest {
    private String peine;
    private String peinesCriminelles;
    private String duree;
    private String lieux;
    private LocalDate date;
    private String description;
}