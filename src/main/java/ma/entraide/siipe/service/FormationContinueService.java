package ma.entraide.siipe.service;

import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.dto.request.FormationContinueRequest;
import ma.entraide.siipe.dto.response.FormationContinueResponse;
import ma.entraide.siipe.entity.FormationContinue;
import ma.entraide.siipe.entity.Personnel;
import ma.entraide.siipe.exception.ResourceNotFoundException;
import ma.entraide.siipe.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormationContinueService {

    private final FormationContinueRepository formationRepo;
    private final EtablissementCentreRepository etablissementRepo;
    private final PersonnelRepository personnelRepo;
    private final FileStorageService fileStorageService;
    private final PersonnelService personnelService;

    public List<FormationContinueResponse> getAll() {
        return formationRepo.findByDeletedFalse().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public List<FormationContinueResponse> getByEtablissement(Long etablissementId) {
        return formationRepo.findByEtablissementCentreIdAndDeletedFalse(etablissementId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public FormationContinueResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Transactional
    public FormationContinueResponse create(FormationContinueRequest request) {
        FormationContinue f = new FormationContinue();
        mapRequestToEntity(request, f);
        return toResponse(formationRepo.save(f));
    }

    @Transactional
    public FormationContinueResponse update(Long id, FormationContinueRequest request) {
        FormationContinue f = findById(id);
        mapRequestToEntity(request, f);
        return toResponse(formationRepo.save(f));
    }

    @Transactional
    public void delete(Long id) {
        FormationContinue f = findById(id);
        f.setDeleted(true);
        formationRepo.save(f);
    }

    @Transactional
    public FormationContinueResponse uploadAttestation(Long id, MultipartFile file) {
        FormationContinue f = findById(id);
        String url = fileStorageService.storeFile(file, "formations/" + id);
        f.setAttestationUrl(url);
        return toResponse(formationRepo.save(f));
    }

    private void mapRequestToEntity(FormationContinueRequest req, FormationContinue f) {
        f.setTitre(req.getTitre());
        f.setDescription(req.getDescription());
        f.setOrganisme(req.getOrganisme());
        f.setDateDebut(req.getDateDebut());
        f.setDateFin(req.getDateFin());
        f.setLieu(req.getLieu());
        f.setDureeJours(req.getDureeJours());

        if (req.getEtablissementCentreId() != null) {
            f.setEtablissementCentre(etablissementRepo.findById(req.getEtablissementCentreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Établissement non trouvé")));
        }
        if (req.getParticipantIds() != null) {
            List<Personnel> participants = personnelRepo.findAllById(req.getParticipantIds());
            f.setParticipants(participants);
        }
    }

    private FormationContinue findById(Long id) {
        return formationRepo.findById(id)
                .filter(f -> !f.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Formation non trouvée"));
    }

    public FormationContinueResponse toResponse(FormationContinue f) {
        return FormationContinueResponse.builder()
                .id(f.getId())
                .titre(f.getTitre())
                .description(f.getDescription())
                .organisme(f.getOrganisme())
                .dateDebut(f.getDateDebut())
                .dateFin(f.getDateFin())
                .lieu(f.getLieu())
                .dureeJours(f.getDureeJours())
                .attestationUrl(f.getAttestationUrl())
                .etablissementCentreId(f.getEtablissementCentre() != null ? f.getEtablissementCentre().getId() : null)
                .etablissementCentreNom(f.getEtablissementCentre() != null ? f.getEtablissementCentre().getNomFr() : null)
                .participants(f.getParticipants() != null ? f.getParticipants().stream()
                        .map(personnelService::toResponse).collect(Collectors.toList()) : List.of())
                .createdAt(f.getCreatedAt())
                .createdBy(f.getCreatedBy())
                .build();
    }
}
