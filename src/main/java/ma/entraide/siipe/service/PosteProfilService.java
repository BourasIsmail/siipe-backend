package ma.entraide.siipe.service;

import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.dto.request.PosteProfilRequest;
import ma.entraide.siipe.dto.response.PosteProfilResponse;
import ma.entraide.siipe.entity.PosteProfil;
import ma.entraide.siipe.exception.ResourceNotFoundException;
import ma.entraide.siipe.repository.EtablissementCentreRepository;
import ma.entraide.siipe.repository.PosteProfilRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosteProfilService {

    private final PosteProfilRepository posteProfilRepo;
    private final EtablissementCentreRepository etablissementRepo;

    public List<PosteProfilResponse> getAll() {
        return posteProfilRepo.findByDeletedFalse().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public List<PosteProfilResponse> getByEtablissement(Long etablissementId) {
        return posteProfilRepo.findByEtablissementCentreIdAndDeletedFalse(etablissementId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public PosteProfilResponse create(PosteProfilRequest request) {
        PosteProfil p = new PosteProfil();
        mapRequestToEntity(request, p);
        return toResponse(posteProfilRepo.save(p));
    }

    public PosteProfilResponse update(Long id, PosteProfilRequest request) {
        PosteProfil p = findById(id);
        mapRequestToEntity(request, p);
        return toResponse(posteProfilRepo.save(p));
    }

    public void delete(Long id) {
        PosteProfil p = findById(id);
        p.setDeleted(true);
        posteProfilRepo.save(p);
    }

    private void mapRequestToEntity(PosteProfilRequest req, PosteProfil p) {
        p.setIntitule(req.getIntitule());
        p.setDescription(req.getDescription());
        p.setFonction(req.getFonction());
        p.setNiveauScolaireRequis(req.getNiveauScolaireRequis());
        p.setCompetencesRequises(req.getCompetencesRequises());
        p.setNombrePostes(req.getNombrePostes());
        p.setNombrePourvu(req.getNombrePourvu());
        if (req.getEtablissementCentreId() != null) {
            p.setEtablissementCentre(etablissementRepo.findById(req.getEtablissementCentreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Établissement non trouvé")));
        }
    }

    private PosteProfil findById(Long id) {
        return posteProfilRepo.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Poste/profil non trouvé"));
    }

    public PosteProfilResponse toResponse(PosteProfil p) {
        return PosteProfilResponse.builder()
                .id(p.getId())
                .intitule(p.getIntitule())
                .description(p.getDescription())
                .fonction(p.getFonction())
                .niveauScolaireRequis(p.getNiveauScolaireRequis())
                .competencesRequises(p.getCompetencesRequises())
                .nombrePostes(p.getNombrePostes())
                .nombrePourvu(p.getNombrePourvu())
                .etablissementCentreId(p.getEtablissementCentre() != null ? p.getEtablissementCentre().getId() : null)
                .etablissementCentreNom(p.getEtablissementCentre() != null ? p.getEtablissementCentre().getNomFr() : null)
                .build();
    }
}
