package ma.entraide.siipe.service;

import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.dto.request.ProgrammeRequest;
import ma.entraide.siipe.dto.response.PrestationResponse;
import ma.entraide.siipe.dto.response.ProgrammeResponse;
import ma.entraide.siipe.entity.Programme;
import ma.entraide.siipe.exception.ResourceNotFoundException;
import ma.entraide.siipe.repository.ProgrammeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgrammeService {

    private final ProgrammeRepository programmeRepo;

    public List<ProgrammeResponse> getAll() {
        return programmeRepo.findByDeletedFalse().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public ProgrammeResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public ProgrammeResponse create(ProgrammeRequest request) {
        Programme p = Programme.builder()
                .nomFr(request.getNomFr())
                .nomAr(request.getNomAr())
                .description(request.getDescription())
                .build();
        return toResponse(programmeRepo.save(p));
    }

    public ProgrammeResponse update(Long id, ProgrammeRequest request) {
        Programme p = findById(id);
        p.setNomFr(request.getNomFr());
        p.setNomAr(request.getNomAr());
        p.setDescription(request.getDescription());
        return toResponse(programmeRepo.save(p));
    }

    public void delete(Long id) {
        Programme p = findById(id);
        p.setDeleted(true);
        programmeRepo.save(p);
    }

    private Programme findById(Long id) {
        return programmeRepo.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Programme non trouvé"));
    }

    public ProgrammeResponse toResponse(Programme p) {
        return ProgrammeResponse.builder()
                .id(p.getId())
                .nomFr(p.getNomFr())
                .nomAr(p.getNomAr())
                .description(p.getDescription())
                .prestations(p.getPrestations() != null ? p.getPrestations().stream()
                        .filter(pr -> !pr.isDeleted())
                        .map(pr -> PrestationResponse.builder()
                                .id(pr.getId())
                                .nomFr(pr.getNomFr())
                                .nomAr(pr.getNomAr())
                                .description(pr.getDescription())
                                .programmeId(p.getId())
                                .programmeNom(p.getNomFr())
                                .build())
                        .collect(Collectors.toList()) : List.of())
                .build();
    }
}
