package ma.entraide.siipe.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.dto.request.PartenaireRequest;
import ma.entraide.siipe.dto.response.PartenaireResponse;
import ma.entraide.siipe.entity.User;
import ma.entraide.siipe.enums.Role;
import ma.entraide.siipe.service.PartenaireService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/partenaires")
@RequiredArgsConstructor
public class PartenaireController {

    private final PartenaireService partenaireService;

    @GetMapping
    public ResponseEntity<List<PartenaireResponse>> getAll(@AuthenticationPrincipal User user) {
        if (user.getRole() == Role.ROLE_DELEGUE) {
            if (user.getProvince() == null) return ResponseEntity.ok(List.of());
            return ResponseEntity.ok(partenaireService.getByProvince(user.getProvince().getId()));
        }
        if (user.getRole() == Role.ROLE_COORDINATION) {
            if (user.getRegion() == null) return ResponseEntity.ok(List.of());
            return ResponseEntity.ok(partenaireService.getByRegion(user.getRegion().getId()));
        }
        return ResponseEntity.ok(partenaireService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartenaireResponse> getById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        PartenaireResponse response = partenaireService.getById(id);
        if (user.getRole() == Role.ROLE_COORDINATION) {
            Long userRegionId = user.getRegion() != null ? user.getRegion().getId() : null;
            if (userRegionId == null || !userRegionId.equals(response.getRegionId())) {
                throw new AccessDeniedException("Accès refusé : ce partenaire n'appartient pas à votre région");
            }
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE','ROLE_CHEF_SERVICE','ROLE_CHEF_DIVISION','ROLE_DIRECTEUR_CENTRALE')")
    public ResponseEntity<PartenaireResponse> create(@Valid @RequestBody PartenaireRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(partenaireService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE','ROLE_CHEF_SERVICE','ROLE_CHEF_DIVISION','ROLE_DIRECTEUR_CENTRALE')")
    public ResponseEntity<PartenaireResponse> update(@PathVariable Long id,
                                                     @Valid @RequestBody PartenaireRequest request) {
        return ResponseEntity.ok(partenaireService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        partenaireService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/logo")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE')")
    public ResponseEntity<PartenaireResponse> uploadLogo(@PathVariable Long id,
                                                         @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(partenaireService.uploadLogo(id, file));
    }
}
