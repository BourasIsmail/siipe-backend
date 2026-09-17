package ma.entraide.siipe.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.dto.request.SubventionRequest;
import ma.entraide.siipe.dto.response.SubventionResponse;
import ma.entraide.siipe.entity.User;
import ma.entraide.siipe.enums.Role;
import ma.entraide.siipe.service.SubventionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/subventions")
@RequiredArgsConstructor
public class SubventionController {

    private final SubventionService subventionService;

    @GetMapping
    public ResponseEntity<List<SubventionResponse>> getAll(@AuthenticationPrincipal User user) {
        if (user.getRole() == Role.ROLE_COORDINATION) {
            if (user.getRegion() == null) return ResponseEntity.ok(List.of());
            return ResponseEntity.ok(subventionService.getByRegion(user.getRegion().getId()));
        }
        return ResponseEntity.ok(subventionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubventionResponse> getById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        SubventionResponse response = subventionService.getById(id);
        if (user.getRole() == Role.ROLE_COORDINATION) {
            Long userRegionId = user.getRegion() != null ? user.getRegion().getId() : null;
            if (userRegionId == null || !userRegionId.equals(response.getRegionId())) {
                throw new AccessDeniedException("Accès refusé : cette subvention n'appartient pas à votre région");
            }
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE','ROLE_CHEF_SERVICE','ROLE_CHEF_DIVISION','ROLE_DIRECTEUR_CENTRALE')")
    public ResponseEntity<SubventionResponse> create(@Valid @RequestBody SubventionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subventionService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE','ROLE_CHEF_SERVICE','ROLE_CHEF_DIVISION','ROLE_DIRECTEUR_CENTRALE')")
    public ResponseEntity<SubventionResponse> update(@PathVariable Long id,
                                                     @Valid @RequestBody SubventionRequest request) {
        return ResponseEntity.ok(subventionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subventionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/convention")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE')")
    public ResponseEntity<SubventionResponse> uploadConvention(@PathVariable Long id,
                                                               @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(subventionService.uploadConvention(id, file));
    }
}
