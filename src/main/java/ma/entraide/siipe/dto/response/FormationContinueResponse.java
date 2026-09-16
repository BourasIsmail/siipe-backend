package ma.entraide.siipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FormationContinueResponse {
    private Long id;
    private String titre;
    private String description;
    private String organisme;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String lieu;
    private Integer dureeJours;
    private String attestationUrl;
    private Long etablissementCentreId;
    private String etablissementCentreNom;
    private List<PersonnelResponse> participants;
    private LocalDateTime createdAt;
    private String createdBy;
}
