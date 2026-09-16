package ma.entraide.siipe.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ma.entraide.siipe.enums.Role;

@Data
public class CreateUserRequest {
    @NotBlank
    private String nom;
    @NotBlank
    private String prenom;
    @NotBlank @Email
    private String email;
    @NotNull
    private Role role;
    private Long regionId;
    private Long provinceId;
    private Long etablissementCentreId;
}
