package ma.entraide.siipe.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.dto.request.PosteProfilRequest;
import ma.entraide.siipe.dto.response.PosteProfilResponse;
import ma.entraide.siipe.service.PosteProfilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postes-profils")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE','ROLE_CHEF_SERVICE','ROLE_CHEF_DIVISION','ROLE_DIRECTEUR_CENTRALE')")
@RequiredArgsConstructor
public class PosteProfilController {

    private final PosteProfilService posteProfilService;

    @GetMapping
    public ResponseEntity<List<PosteProfilResponse>> getAll() {
        return ResponseEntity.ok(posteProfilService.getAll());
    }

    @GetMapping("/etablissement/{etablissementId}")
    public ResponseEntity<List<PosteProfilResponse>> getByEtablissement(@PathVariable Long etablissementId) {
        return ResponseEntity.ok(posteProfilService.getByEtablissement(etablissementId));
    }

    @PostMapping
    public ResponseEntity<PosteProfilResponse> create(@Valid @RequestBody PosteProfilRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(posteProfilService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PosteProfilResponse> update(@PathVariable Long id,
                                                      @Valid @RequestBody PosteProfilRequest request) {
        return ResponseEntity.ok(posteProfilService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        posteProfilService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
