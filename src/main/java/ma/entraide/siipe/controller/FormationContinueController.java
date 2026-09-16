package ma.entraide.siipe.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.dto.request.FormationContinueRequest;
import ma.entraide.siipe.dto.response.FormationContinueResponse;
import ma.entraide.siipe.service.FormationContinueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/formations")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE','ROLE_CHEF_SERVICE','ROLE_CHEF_DIVISION','ROLE_DIRECTEUR_CENTRALE')")
@RequiredArgsConstructor
public class FormationContinueController {

    private final FormationContinueService formationService;

    @GetMapping
    public ResponseEntity<List<FormationContinueResponse>> getAll() {
        return ResponseEntity.ok(formationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormationContinueResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(formationService.getById(id));
    }

    @GetMapping("/etablissement/{etablissementId}")
    public ResponseEntity<List<FormationContinueResponse>> getByEtablissement(@PathVariable Long etablissementId) {
        return ResponseEntity.ok(formationService.getByEtablissement(etablissementId));
    }

    @PostMapping
    public ResponseEntity<FormationContinueResponse> create(@Valid @RequestBody FormationContinueRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(formationService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FormationContinueResponse> update(@PathVariable Long id,
                                                            @Valid @RequestBody FormationContinueRequest request) {
        return ResponseEntity.ok(formationService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        formationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/attestation")
    public ResponseEntity<FormationContinueResponse> uploadAttestation(@PathVariable Long id,
                                                                       @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(formationService.uploadAttestation(id, file));
    }
}
