package ma.entraide.siipe.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.dto.request.ProgrammeRequest;
import ma.entraide.siipe.dto.request.PrestationRequest;
import ma.entraide.siipe.dto.response.ProgrammeResponse;
import ma.entraide.siipe.dto.response.PrestationResponse;
import ma.entraide.siipe.service.PrestationService;
import ma.entraide.siipe.service.ProgrammeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programmes")
@RequiredArgsConstructor
public class ProgrammeController {

    private final ProgrammeService programmeService;
    private final PrestationService prestationService;

    @GetMapping
    public ResponseEntity<List<ProgrammeResponse>> getAll() {
        return ResponseEntity.ok(programmeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgrammeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(programmeService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProgrammeResponse> create(@Valid @RequestBody ProgrammeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(programmeService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProgrammeResponse> update(@PathVariable Long id,
                                                    @Valid @RequestBody ProgrammeRequest request) {
        return ResponseEntity.ok(programmeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        programmeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Prestation endpoints under programme
    @GetMapping("/{programmeId}/prestations")
    public ResponseEntity<List<PrestationResponse>> getPrestationsByProgramme(@PathVariable Long programmeId) {
        return ResponseEntity.ok(prestationService.getByProgramme(programmeId));
    }

    @PostMapping("/{programmeId}/prestations")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PrestationResponse> createPrestation(
            @PathVariable Long programmeId,
            @RequestBody PrestationRequest request) {
        request.setProgrammeId(programmeId);  // ← set it from path
        return ResponseEntity.status(HttpStatus.CREATED).body(prestationService.create(request));
    }

    @PutMapping("/prestations/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PrestationResponse> updatePrestation(@PathVariable Long id,
                                                               @Valid @RequestBody PrestationRequest request) {
        return ResponseEntity.ok(prestationService.update(id, request));
    }

    @DeleteMapping("/prestations/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deletePrestation(@PathVariable Long id) {
        prestationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
