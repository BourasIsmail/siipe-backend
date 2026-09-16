package ma.entraide.siipe.service;

import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.dto.request.PrestationRequest;
import ma.entraide.siipe.dto.response.PrestationResponse;
import ma.entraide.siipe.entity.Prestation;
import ma.entraide.siipe.entity.Programme;
import ma.entraide.siipe.exception.ResourceNotFoundException;
import ma.entraide.siipe.repository.PrestationRepository;
import ma.entraide.siipe.repository.ProgrammeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrestationService {

    private final PrestationRepository prestationRepo;
    private final ProgrammeRepository programmeRepo;

    public List<PrestationResponse> getAll() {
        return prestationRepo.findByDeletedFalse().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public List<PrestationResponse> getByProgramme(Long programmeId) {
        return prestationRepo.findByProgrammeIdAndDeletedFalse(programmeId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public PrestationResponse create(PrestationRequest request) {
        Programme programme = programmeRepo.findById(request.getProgrammeId())
                .orElseThrow(() -> new ResourceNotFoundException("Programme non trouvé"));

        Prestation p = Prestation.builder()
                .nomFr(request.getNomFr())
                .nomAr(request.getNomAr())
                .description(request.getDescription())
                .programme(programme)
                .build();
        return toResponse(prestationRepo.save(p));
    }

    public PrestationResponse update(Long id, PrestationRequest request) {
        Prestation p = findById(id);
        p.setNomFr(request.getNomFr());
        p.setNomAr(request.getNomAr());
        p.setDescription(request.getDescription());
        if (request.getProgrammeId() != null) {
            Programme programme = programmeRepo.findById(request.getProgrammeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Programme non trouvé"));
            p.setProgramme(programme);
        }
        return toResponse(prestationRepo.save(p));
    }

    public void delete(Long id) {
        Prestation p = findById(id);
        p.setDeleted(true);
        prestationRepo.save(p);
    }

    private Prestation findById(Long id) {
        return prestationRepo.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Prestation non trouvée"));
    }

    public PrestationResponse toResponse(Prestation p) {
        return PrestationResponse.builder()
                .id(p.getId())
                .nomFr(p.getNomFr())
                .nomAr(p.getNomAr())
                .description(p.getDescription())
                .programmeId(p.getProgramme().getId())
                .programmeNom(p.getProgramme().getNomFr())
                .build();
    }
}
