package ma.entraide.siipe.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.dto.request.*;
import ma.entraide.siipe.dto.response.*;
import ma.entraide.siipe.entity.User;
import ma.entraide.siipe.enums.Sexe;
import ma.entraide.siipe.enums.SituationDifficulte;
import ma.entraide.siipe.service.BeneficiaireService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/beneficiaires")
@RequiredArgsConstructor
public class BeneficiaireController {

    private final BeneficiaireService beneficiaireService;

    @GetMapping
    public ResponseEntity<List<BeneficiaireResponse>> getAll(@AuthenticationPrincipal User user) {
        if (user.getProvince() != null &&
                user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DELEGUE"))) {
            return ResponseEntity.ok(beneficiaireService.getByProvince(user.getProvince().getId()));
        }
        return ResponseEntity.ok(beneficiaireService.getAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<BeneficiaireResponse>> search(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String cin,
            @RequestParam(required = false) Sexe sexe,
            @RequestParam(required = false) SituationDifficulte situationDifficulte,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateNaissanceFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateNaissanceTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEntreeFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEntreeTo,
            @RequestParam(required = false) Long etablissementId,
            @RequestParam(required = false) Long provinceId,
            @RequestParam(required = false) String typeHandicap,
            @AuthenticationPrincipal User user) {

        Long filteredProvinceId = provinceId;
        if (user.getProvince() != null &&
                user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DELEGUE"))) {
            filteredProvinceId = user.getProvince().getId();
        }

        return ResponseEntity.ok(beneficiaireService.search(
                nom, prenom, cin, sexe, situationDifficulte,
                dateNaissanceFrom, dateNaissanceTo,
                dateEntreeFrom, dateEntreeTo,
                etablissementId, filteredProvinceId, typeHandicap));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeneficiaireResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(beneficiaireService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ASSISTANTE_SOCIALE')")
    public ResponseEntity<BeneficiaireResponse> create(@Valid @RequestBody BeneficiaireRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficiaireService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ASSISTANTE_SOCIALE')")
    public ResponseEntity<BeneficiaireResponse> update(@PathVariable Long id,
                                                       @Valid @RequestBody BeneficiaireRequest request) {
        return ResponseEntity.ok(beneficiaireService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELEGUE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        beneficiaireService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/photo")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ASSISTANTE_SOCIALE')")
    public ResponseEntity<BeneficiaireResponse> uploadPhoto(@PathVariable Long id,
                                                            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(beneficiaireService.uploadPhoto(id, file));
    }

    // --- Sub-situations ---

    @GetMapping("/{id}/situations-medicales")
    public ResponseEntity<List<SituationMedicaleResponse>> getSituationsMedicales(@PathVariable Long id) {
        return ResponseEntity.ok(beneficiaireService.getSituationsMedicales(id));
    }

    @PostMapping("/{id}/situations-medicales")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ASSISTANTE_SOCIALE')")
    public ResponseEntity<SituationMedicaleResponse> addSituationMedicale(@PathVariable Long id,
                                                                          @RequestBody SituationMedicaleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficiaireService.addSituationMedicale(id, request));
    }

    @PostMapping("/{id}/situations-medicales/{situationId}/certificat")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ASSISTANTE_SOCIALE')")
    public ResponseEntity<SituationMedicaleResponse> uploadCertificatMedical(
            @PathVariable Long id, @PathVariable Long situationId,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(beneficiaireService.uploadCertificatMedical(situationId, file));
    }

    @GetMapping("/{id}/situations-sociales")
    public ResponseEntity<List<SituationSocialeResponse>> getSituationsSociales(@PathVariable Long id) {
        return ResponseEntity.ok(beneficiaireService.getSituationsSociales(id));
    }

    @PostMapping("/{id}/situations-sociales")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ASSISTANTE_SOCIALE')")
    public ResponseEntity<SituationSocialeResponse> addSituationSociale(@PathVariable Long id,
                                                                        @RequestBody SituationSocialeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficiaireService.addSituationSociale(id, request));
    }

    @GetMapping("/{id}/situations-judiciaires")
    public ResponseEntity<List<SituationJudiciaireResponse>> getSituationsJudiciaires(@PathVariable Long id) {
        return ResponseEntity.ok(beneficiaireService.getSituationsJudiciaires(id));
    }

    @PostMapping("/{id}/situations-judiciaires")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ASSISTANTE_SOCIALE')")
    public ResponseEntity<SituationJudiciaireResponse> addSituationJudiciaire(@PathVariable Long id,
                                                                              @RequestBody SituationJudiciaireRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficiaireService.addSituationJudiciaire(id, request));
    }

    @PostMapping("/{id}/situations-judiciaires/{situationId}/piece-jointe")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ASSISTANTE_SOCIALE')")
    public ResponseEntity<SituationJudiciaireResponse> uploadPieceJointeJudiciaire(
            @PathVariable Long id, @PathVariable Long situationId,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(beneficiaireService.uploadPieceJointeJudiciaire(situationId, file));
    }

    @GetMapping("/{id}/dossiers-scolaires")
    public ResponseEntity<List<DossierScolaireResponse>> getDossiersScolaires(@PathVariable Long id) {
        return ResponseEntity.ok(beneficiaireService.getDossiersScolaires(id));
    }

    @PostMapping("/{id}/dossiers-scolaires")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ASSISTANTE_SOCIALE')")
    public ResponseEntity<DossierScolaireResponse> addDossierScolaire(@PathVariable Long id,
                                                                      @RequestBody DossierScolaireRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficiaireService.addDossierScolaire(id, request));
    }

    @PostMapping("/{id}/dossiers-scolaires/{dossierId}/piece-jointe")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ASSISTANTE_SOCIALE')")
    public ResponseEntity<DossierScolaireResponse> uploadPieceJointeDossier(
            @PathVariable Long id, @PathVariable Long dossierId,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(beneficiaireService.uploadDossierFile(dossierId, file));
    }

    @GetMapping("/{id}/accompagnements")
    public ResponseEntity<List<AccompagnementResponse>> getAccompagnements(@PathVariable Long id) {
        return ResponseEntity.ok(beneficiaireService.getAccompagnements(id));
    }

    @PostMapping("/{id}/accompagnements")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ASSISTANTE_SOCIALE')")
    public ResponseEntity<AccompagnementResponse> addAccompagnement(@PathVariable Long id,
                                                                    @RequestBody AccompagnementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficiaireService.addAccompagnement(id, request));
    }

    @GetMapping("/{id}/besoins")
    public ResponseEntity<List<BesoinExprimeResponse>> getBesoins(@PathVariable Long id) {
        return ResponseEntity.ok(beneficiaireService.getBesoinsExprimes(id));
    }

    @PostMapping("/{id}/besoins")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ASSISTANTE_SOCIALE')")
    public ResponseEntity<BesoinExprimeResponse> addBesoin(@PathVariable Long id,
                                                           @RequestBody BesoinExprimeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficiaireService.addBesoinExprime(id, request));
    }

    @GetMapping("/{id}/prestations-beneficiaire")
    public ResponseEntity<List<PrestationBeneficiaireResponse>> getPrestationsBeneficiaire(@PathVariable Long id) {
        return ResponseEntity.ok(beneficiaireService.getPrestationsBeneficiaire(id));
    }

    @PostMapping("/{id}/prestations-beneficiaire")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ASSISTANTE_SOCIALE')")
    public ResponseEntity<PrestationBeneficiaireResponse> addPrestationBeneficiaire(
            @PathVariable Long id,
            @RequestBody PrestationBeneficiaireRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(beneficiaireService.addPrestationBeneficiaire(id, request));
    }

    @PostMapping("/{id}/prestations-beneficiaire/{prestationId}/piece-jointe")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ASSISTANTE_SOCIALE')")
    public ResponseEntity<PrestationBeneficiaireResponse> uploadPieceJointePrestation(
            @PathVariable Long id, @PathVariable Long prestationId,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(beneficiaireService.uploadPrestationFile(prestationId, file));
    }
}