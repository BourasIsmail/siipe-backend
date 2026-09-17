package ma.entraide.siipe.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.dto.request.PersonnelRequest;
import ma.entraide.siipe.dto.response.PersonnelResponse;
import ma.entraide.siipe.entity.User;
import ma.entraide.siipe.enums.Role;
import ma.entraide.siipe.service.PersonnelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/personnel")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE','ROLE_CHEF_SERVICE','ROLE_CHEF_DIVISION','ROLE_DIRECTEUR_CENTRALE')")
@RequiredArgsConstructor
public class PersonnelController {

    private final PersonnelService personnelService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE','ROLE_CHEF_SERVICE','ROLE_CHEF_DIVISION','ROLE_DIRECTEUR_CENTRALE','ROLE_COORDINATION')")
    public ResponseEntity<List<PersonnelResponse>> getAll(@AuthenticationPrincipal User user) {
        // DELEGUE only sees personnel from their province
        if (user.getRole() == Role.ROLE_DELEGUE) {
            if (user.getProvince() == null) return ResponseEntity.ok(List.of());
            return ResponseEntity.ok(personnelService.getByProvince(user.getProvince().getId()));
        }
        // COORDINATION only sees personnel from their region
        if (user.getRole() == Role.ROLE_COORDINATION) {
            if (user.getRegion() == null) return ResponseEntity.ok(List.of());
            return ResponseEntity.ok(personnelService.getByRegion(user.getRegion().getId()));
        }
        return ResponseEntity.ok(personnelService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE','ROLE_CHEF_SERVICE','ROLE_CHEF_DIVISION','ROLE_DIRECTEUR_CENTRALE','ROLE_COORDINATION')")
    public ResponseEntity<PersonnelResponse> getById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        PersonnelResponse response = personnelService.getById(id);
        checkScope(user, response.getProvinceId(), response.getRegionId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/etablissement/{etablissementId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE','ROLE_CHEF_SERVICE','ROLE_CHEF_DIVISION','ROLE_DIRECTEUR_CENTRALE','ROLE_COORDINATION')")
    public ResponseEntity<List<PersonnelResponse>> getByEtablissement(@PathVariable Long etablissementId) {
        return ResponseEntity.ok(personnelService.getByEtablissement(etablissementId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CHEF_SERVICE','ROLE_CHEF_DIVISION','ROLE_DIRECTEUR_CENTRALE')")
    public ResponseEntity<PersonnelResponse> create(@Valid @RequestBody PersonnelRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personnelService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CHEF_SERVICE','ROLE_CHEF_DIVISION','ROLE_DIRECTEUR_CENTRALE')")
    public ResponseEntity<PersonnelResponse> update(@PathVariable Long id,
                                                    @Valid @RequestBody PersonnelRequest request) {
        return ResponseEntity.ok(personnelService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        personnelService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/photo")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CHEF_SERVICE','ROLE_CHEF_DIVISION','ROLE_DIRECTEUR_CENTRALE')")
    public ResponseEntity<PersonnelResponse> uploadPhoto(@PathVariable Long id,
                                                         @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(personnelService.uploadPhoto(id, file));
    }

    private void checkScope(User user, Long provinceId, Long regionId) {
        if (user.getRole() == Role.ROLE_DELEGUE) {
            Long userProvinceId = user.getProvince() != null ? user.getProvince().getId() : null;
            if (userProvinceId == null || !userProvinceId.equals(provinceId)) {
                throw new AccessDeniedException("Accès refusé : ce personnel n'appartient pas à votre province");
            }
        } else if (user.getRole() == Role.ROLE_COORDINATION) {
            Long userRegionId = user.getRegion() != null ? user.getRegion().getId() : null;
            if (userRegionId == null || !userRegionId.equals(regionId)) {
                throw new AccessDeniedException("Accès refusé : ce personnel n'appartient pas à votre région");
            }
        }
    }
}
