package ma.entraide.siipe.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.dto.request.EtablissementRequest;
import ma.entraide.siipe.dto.response.EtablissementResponse;
import ma.entraide.siipe.entity.User;
import ma.entraide.siipe.service.EtablissementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/etablissements")
@RequiredArgsConstructor
public class EtablissementController {

    private final EtablissementService etablissementService;

    @GetMapping
    public ResponseEntity<List<EtablissementResponse>> getAll(@AuthenticationPrincipal User user) {
        // DELEGUE sees only their province
        if (user.getProvince() != null &&
                user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DELEGUE"))) {
            return ResponseEntity.ok(etablissementService.getByProvince(user.getProvince().getId()));
        }
        return ResponseEntity.ok(etablissementService.getAll());
    }

    @GetMapping("/map")
    public ResponseEntity<List<EtablissementResponse>> getForMap(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(etablissementService.getForMap());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EtablissementResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(etablissementService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE','ROLE_CHEF_SERVICE','ROLE_CHEF_DIVISION','ROLE_DIRECTEUR_CENTRALE')")
    public ResponseEntity<EtablissementResponse> create(@Valid @RequestBody EtablissementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(etablissementService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE','ROLE_CHEF_SERVICE','ROLE_CHEF_DIVISION','ROLE_DIRECTEUR_CENTRALE')")
    public ResponseEntity<EtablissementResponse> update(@PathVariable Long id,
                                                        @Valid @RequestBody EtablissementRequest request) {
        return ResponseEntity.ok(etablissementService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        etablissementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/files/{fileType}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE','ROLE_CHEF_SERVICE','ROLE_CHEF_DIVISION','ROLE_DIRECTEUR_CENTRALE')")
    public ResponseEntity<EtablissementResponse> uploadFile(@PathVariable Long id,
                                                            @PathVariable String fileType,
                                                            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(etablissementService.uploadFile(id, file, fileType));
    }
}
