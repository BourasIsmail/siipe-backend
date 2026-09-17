package ma.entraide.siipe.service;

import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.dto.request.SubventionRequest;
import ma.entraide.siipe.dto.response.SubventionResponse;
import ma.entraide.siipe.entity.Subvention;
import ma.entraide.siipe.exception.ResourceNotFoundException;
import ma.entraide.siipe.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubventionService {

    private final SubventionRepository subventionRepo;
    private final PartenaireRepository partenaireRepo;
    private final EtablissementCentreRepository etablissementRepo;
    private final ProgrammeRepository programmeRepo;
    private final FileStorageService fileStorageService;

    public List<SubventionResponse> getAll() {
        return subventionRepo.findByDeletedFalse().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public SubventionResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public List<SubventionResponse> getByRegion(Long regionId) {
        return subventionRepo.findByEtablissementRegionIdAndDeletedFalse(regionId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public SubventionResponse create(SubventionRequest request) {
        Subvention s = new Subvention();
        mapRequestToEntity(request, s);
        return toResponse(subventionRepo.save(s));
    }

    public SubventionResponse update(Long id, SubventionRequest request) {
        Subvention s = findById(id);
        mapRequestToEntity(request, s);
        return toResponse(subventionRepo.save(s));
    }

    public void delete(Long id) {
        Subvention s = findById(id);
        s.setDeleted(true);
        subventionRepo.save(s);
    }

    public SubventionResponse uploadConvention(Long id, MultipartFile file) {
        Subvention s = findById(id);
        String url = fileStorageService.storeFile(file, "subventions/" + id);
        s.setConventionUrl(url);
        return toResponse(subventionRepo.save(s));
    }

    private void mapRequestToEntity(SubventionRequest request, Subvention s) {
        s.setTitre(request.getTitre());
        s.setDescription(request.getDescription());
        s.setMontant(request.getMontant());
        s.setDateDebut(request.getDateDebut());
        s.setDateFin(request.getDateFin());
        s.setStatut(request.getStatut());

        if (request.getPartenaireId() != null) {
            s.setPartenaire(partenaireRepo.findById(request.getPartenaireId())
                    .orElseThrow(() -> new ResourceNotFoundException("Partenaire non trouvé")));
        }
        if (request.getEtablissementId() != null) {
            s.setEtablissement(etablissementRepo.findById(request.getEtablissementId())
                    .orElseThrow(() -> new ResourceNotFoundException("Établissement non trouvé")));
        }
        if (request.getProgrammeId() != null) {
            s.setProgramme(programmeRepo.findById(request.getProgrammeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Programme non trouvé")));
        }
    }

    private Subvention findById(Long id) {
        return subventionRepo.findById(id)
                .filter(s -> !s.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Subvention non trouvée"));
    }

    public SubventionResponse toResponse(Subvention s) {
        return SubventionResponse.builder()
                .id(s.getId())
                .titre(s.getTitre())
                .description(s.getDescription())
                .montant(s.getMontant())
                .dateDebut(s.getDateDebut())
                .dateFin(s.getDateFin())
                .statut(s.getStatut())
                .conventionUrl(s.getConventionUrl())
                .partenaireId(s.getPartenaire() != null ? s.getPartenaire().getId() : null)
                .partenaireNom(s.getPartenaire() != null ? s.getPartenaire().getNomFr() : null)
                .etablissementId(s.getEtablissement() != null ? s.getEtablissement().getId() : null)
                .etablissementNom(s.getEtablissement() != null ? s.getEtablissement().getNomFr() : null)
                .regionId(s.getEtablissement() != null && s.getEtablissement().getRegion() != null
                        ? s.getEtablissement().getRegion().getId() : null)
                .regionNom(s.getEtablissement() != null && s.getEtablissement().getRegion() != null
                        ? s.getEtablissement().getRegion().getNomFr() : null)
                .programmeId(s.getProgramme() != null ? s.getProgramme().getId() : null)
                .programmeNom(s.getProgramme() != null ? s.getProgramme().getNomFr() : null)
                .createdAt(s.getCreatedAt())
                .createdBy(s.getCreatedBy())
                .build();
    }
}
