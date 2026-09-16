package ma.entraide.siipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String nom;
    private String prenom;
    private String email;
    private String role;
    private boolean active;
    private Long regionId;
    private String regionNom;
    private Long provinceId;
    private String provinceNom;
    private LocalDateTime createdAt;
}
