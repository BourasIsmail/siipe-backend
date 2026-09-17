package ma.entraide.siipe.service;

import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.dto.request.PartenaireRequest;
import ma.entraide.siipe.dto.response.PartenaireResponse;
import ma.entraide.siipe.entity.Partenaire;
import ma.entraide.siipe.entity.Province;
import ma.entraide.siipe.entity.Region;
import ma.entraide.siipe.exception.ResourceNotFoundException;
import ma.entraide.siipe.repository.PartenaireRepository;
import ma.entraide.siipe.repository.ProvinceRepository;
import ma.entraide.siipe.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartenaireService {

    private final PartenaireRepository partenaireRepo;
    private final ProvinceRepository provinceRepo;
    private final RegionRepository regionRepo;
    private final FileStorageService fileStorageService;

    public List<PartenaireResponse> getAll() {
        return partenaireRepo.findByDeletedFalse().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public List<PartenaireResponse> getByProvince(Long provinceId) {
        return partenaireRepo.findByProvinceIdAndDeletedFalse(provinceId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public List<PartenaireResponse> getByRegion(Long regionId) {
        return partenaireRepo.findByRegionIdAndDeletedFalse(regionId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public PartenaireResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public PartenaireResponse create(PartenaireRequest request) {
        Partenaire p = new Partenaire();
        mapRequestToEntity(request, p);
        return toResponse(partenaireRepo.save(p));
    }

    public PartenaireResponse update(Long id, PartenaireRequest request) {
        Partenaire p = findById(id);
        mapRequestToEntity(request, p);
        return toResponse(partenaireRepo.save(p));
    }

    public void delete(Long id) {
        Partenaire p = findById(id);
        p.setDeleted(true);
        partenaireRepo.save(p);
    }

    public PartenaireResponse uploadLogo(Long id, MultipartFile file) {
        Partenaire p = findById(id);
        String url = fileStorageService.storeFile(file, "partenaires/" + id);
        p.setLogoUrl(url);
        return toResponse(partenaireRepo.save(p));
    }

    private void mapRequestToEntity(PartenaireRequest request, Partenaire p) {
        p.setNomFr(request.getNomFr());
        p.setNomAr(request.getNomAr());
        p.setType(request.getType());
        p.setTelephone(request.getTelephone());
        p.setEmail(request.getEmail());
        p.setAdresse(request.getAdresse());
        p.setResponsable(request.getResponsable());
        p.setDescription(request.getDescription());

        if (request.getProvinceId() != null) {
            Province province = provinceRepo.findById(request.getProvinceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Province non trouvée"));
            p.setProvince(province);
        }
        if (request.getRegionId() != null) {
            Region region = regionRepo.findById(request.getRegionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Région non trouvée"));
            p.setRegion(region);
        }
    }

    private Partenaire findById(Long id) {
        return partenaireRepo.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Partenaire non trouvé"));
    }

    public PartenaireResponse toResponse(Partenaire p) {
        return PartenaireResponse.builder()
                .id(p.getId())
                .nomFr(p.getNomFr())
                .nomAr(p.getNomAr())
                .type(p.getType())
                .telephone(p.getTelephone())
                .email(p.getEmail())
                .adresse(p.getAdresse())
                .responsable(p.getResponsable())
                .description(p.getDescription())
                .logoUrl(p.getLogoUrl())
                .provinceId(p.getProvince() != null ? p.getProvince().getId() : null)
                .provinceNom(p.getProvince() != null ? p.getProvince().getNomFr() : null)
                .regionId(p.getRegion() != null ? p.getRegion().getId() : null)
                .regionNom(p.getRegion() != null ? p.getRegion().getNomFr() : null)
                .createdAt(p.getCreatedAt())
                .createdBy(p.getCreatedBy())
                .build();
    }
}
