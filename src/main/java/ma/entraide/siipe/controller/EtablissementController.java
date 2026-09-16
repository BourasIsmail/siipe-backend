package ma.entraide.siipe.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.dto.request.EtablissementRequest;
import ma.entraide.siipe.dto.response.EtablissementResponse;
import ma.entraide.siipe.entity.User;
import ma.entraide.siipe.enums.Role;
import ma.entraide.siipe.service.EtablissementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
        if (user.getRole() == Role.ROLE_DELEGUE) {
            if (user.getProvince() == null) return ResponseEntity.ok(List.of());
            return ResponseEntity.ok(etablissementService.getByProvince(user.getProvince().getId()));
        }
        return ResponseEntity.ok(etablissementService.getAll());
    }

    @GetMapping("/map")
    public ResponseEntity<List<EtablissementResponse>> getForMap(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(etablissementService.getForMap());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EtablissementResponse> getById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        EtablissementResponse response = etablissementService.getById(id);
        checkProvinceScope(user, response.getProvinceId());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE','ROLE_CHEF_SERVICE','ROLE_CHEF_DIVISION','ROLE_DIRECTEUR_CENTRALE')")
    public ResponseEntity<EtablissementResponse> create(@Valid @RequestBody EtablissementRequest request,
                                                         @AuthenticationPrincipal User user) {
        checkProvinceScope(user, request.getProvinceId());
        return ResponseEntity.status(HttpStatus.CREATED).body(etablissementService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE','ROLE_CHEF_SERVICE','ROLE_CHEF_DIVISION','ROLE_DIRECTEUR_CENTRALE')")
    public ResponseEntity<EtablissementResponse> update(@PathVariable Long id,
                                                        @Valid @RequestBody EtablissementRequest request,
                                                        @AuthenticationPrincipal User user) {
        checkProvinceScope(user, etablissementService.getById(id).getProvinceId());
        checkProvinceScope(user, request.getProvinceId());
        return ResponseEntity.ok(etablissementService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE')")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        checkProvinceScope(user, etablissementService.getById(id).getProvinceId());
        etablissementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/files/{fileType}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE','ROLE_CHEF_SERVICE','ROLE_CHEF_DIVISION','ROLE_DIRECTEUR_CENTRALE')")
    public ResponseEntity<EtablissementResponse> uploadFile(@PathVariable Long id,
                                                            @PathVariable String fileType,
                                                            @RequestParam("file") MultipartFile file,
                                                            @AuthenticationPrincipal User user) {
        checkProvinceScope(user, etablissementService.getById(id).getProvinceId());
        return ResponseEntity.ok(etablissementService.uploadFile(id, file, fileType));
    }

    private void checkProvinceScope(User user, Long provinceId) {
        if (user.getRole() == Role.ROLE_DELEGUE) {
            Long userProvinceId = user.getProvince() != null ? user.getProvince().getId() : null;
            if (userProvinceId == null || !userProvinceId.equals(provinceId)) {
                throw new AccessDeniedException("Accès refusé : cet établissement n'appartient pas à votre province");
            }
        }
    }
}
