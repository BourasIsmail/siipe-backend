package ma.entraide.siipe.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.dto.request.PersonnelRequest;
import ma.entraide.siipe.dto.response.PersonnelResponse;
import ma.entraide.siipe.entity.User;
import ma.entraide.siipe.service.PersonnelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<PersonnelResponse>> getAll(@AuthenticationPrincipal User user) {
        // DELEGUE only sees personnel from their province
        if (user.getProvince() != null &&
                user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DELEGUE"))) {
            return ResponseEntity.ok(personnelService.getByProvince(user.getProvince().getId()));
        }
        return ResponseEntity.ok(personnelService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonnelResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(personnelService.getById(id));
    }

    @GetMapping("/etablissement/{etablissementId}")
    public ResponseEntity<List<PersonnelResponse>> getByEtablissement(@PathVariable Long etablissementId) {
        return ResponseEntity.ok(personnelService.getByEtablissement(etablissementId));
    }

    @PostMapping
    public ResponseEntity<PersonnelResponse> create(@Valid @RequestBody PersonnelRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personnelService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonnelResponse> update(@PathVariable Long id,
                                                    @Valid @RequestBody PersonnelRequest request) {
        return ResponseEntity.ok(personnelService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        personnelService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/photo")
    public ResponseEntity<PersonnelResponse> uploadPhoto(@PathVariable Long id,
                                                         @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(personnelService.uploadPhoto(id, file));
    }
}
