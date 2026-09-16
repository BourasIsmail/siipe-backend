package ma.entraide.siipe.service;

import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.dto.request.EtablissementRequest;
import ma.entraide.siipe.dto.response.EtablissementResponse;
import ma.entraide.siipe.dto.response.PrestationResponse;
import ma.entraide.siipe.dto.response.ProgrammeResponse;
import ma.entraide.siipe.entity.*;
import ma.entraide.siipe.exception.ResourceNotFoundException;
import ma.entraide.siipe.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EtablissementService {

    private final EtablissementCentreRepository etablissementRepo;
    private final ProvinceRepository provinceRepo;
    private final RegionRepository regionRepo;
    private final ProgrammeRepository programmeRepo;
    private final PartenaireRepository partenaireRepo;
    private final FileStorageService fileStorageService;
    private final PrestationRepository prestationRepository;

    public List<EtablissementResponse> getAll() {
        return etablissementRepo.findByDeletedFalse().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public List<EtablissementResponse> getByProvince(Long provinceId) {
        return etablissementRepo.findByProvinceIdAndDeletedFalse(provinceId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public EtablissementResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public List<EtablissementResponse> getForMap() {
        return etablissementRepo.findAllWithCoordinates().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public EtablissementResponse create(EtablissementRequest request) {
        EtablissementCentre e = new EtablissementCentre();
        mapRequestToEntity(request, e);
        return toResponse(etablissementRepo.save(e));
    }

    @Transactional
    public EtablissementResponse update(Long id, EtablissementRequest request) {
        EtablissementCentre e = findById(id);
        mapRequestToEntity(request, e);
        return toResponse(etablissementRepo.save(e));
    }

    @Transactional
    public void delete(Long id) {
        EtablissementCentre e = findById(id);
        e.setDeleted(true);
        etablissementRepo.save(e);
    }

    @Transactional
    public EtablissementResponse uploadFile(Long id, MultipartFile file, String fileType) {
        EtablissementCentre e = findById(id);
        String url = fileStorageService.storeFile(file, "etablissements/" + id);

        switch (fileType) {
            case "photo" -> e.setPhotoUrl(url);
            case "mappeCadastrale" -> e.setMappeCadastraleUrl(url);
            case "certificatPropriete" -> e.setCertificatProprieteUrl(url);
            case "planSituation" -> e.setPlanSituationUrl(url);
            case "planArchitecture" -> e.setPlanArchitectureUrl(url);
            default -> throw new ResourceNotFoundException("Type de fichier inconnu: " + fileType);
        }

        return toResponse(etablissementRepo.save(e));
    }

    private EtablissementCentre findById(Long id) {
        return etablissementRepo.findById(id)
                .filter(e -> !e.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Établissement non trouvé"));
    }

    private void mapRequestToEntity(EtablissementRequest request, EtablissementCentre e) {
        e.setNomFr(request.getNomFr());
        e.setNomAr(request.getNomAr());
        e.setCode(request.getCode());
        e.setTelephone(request.getTelephone());
        e.setFax(request.getFax());
        e.setAdresse(request.getAdresse());
        e.setMilieu(request.getMilieu());
        e.setTypeLocal(request.getTypeLocal());
        e.setProprietecentre(request.getProprietecentre());
        e.setGererPar(request.getGererPar());
        e.setPersonneResponsableCentre(request.getPersonneResponsableCentre());
        e.setLatitude(request.getLatitude());
        e.setLongitude(request.getLongitude());
        e.setDateConstruction(request.getDateConstruction());
        e.setDateExploitation(request.getDateExploitation());
        e.setDateAchat(request.getDateAchat());
        e.setSuperficieTerrain(request.getSuperficieTerrain());
        e.setSurfaceBatie(request.getSurfaceBatie());
        e.setSuperficieTotaleEtages(request.getSuperficieTotaleEtages());
        e.setNombreEtage(request.getNombreEtage());
        e.setEtagesUtilises(request.getEtagesUtilises());
        e.setComposant(request.getComposant());
        e.setCapaciteAccueil(request.getCapaciteAccueil());
        e.setEtatConstruction(request.getEtatConstruction());
        e.setObservation(request.getObservation());
        e.setNumerotitre(request.getNumerotitre());
        e.setLoyer(request.getLoyer());
        e.setMontantLoyer(request.getMontantLoyer());
        e.setPaieLoyer(request.getPaieLoyer());
        e.setRaccordementEauPotable(request.getRaccordementEauPotable());
        e.setRaccordementElectricite(request.getRaccordementElectricite());
        e.setPlanSituation(request.getPlanSituation());
        e.setPlanArchitecture(request.getPlanArchitecture());
        e.setLitige(request.getLitige());
        e.setRaisonsConflit(request.getRaisonsConflit());
        e.setPrixBatiment(request.getPrixBatiment());
        e.setAutorise(request.getAutorise());
        e.setNumeroAutorisation(request.getNumeroAutorisation());
        e.setUtilisation(request.getUtilisation());
        if (request.getPrestationIds() != null) {
            List<Prestation> prestations = prestationRepository.findAllById(request.getPrestationIds());
            e.setPrestations(prestations);
        }

        if (request.getProvinceId() != null) {
            Province province = provinceRepo.findById(request.getProvinceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Province non trouvée"));
            e.setProvince(province);
            e.setRegion(province.getRegion());
        }

        if (request.getProgrammeIds() != null) {
            List<Programme> programmes = programmeRepo.findAllById(request.getProgrammeIds());
            e.setProgrammes(programmes);
        }

        if (request.getPartenaireIds() != null) {
            List<Partenaire> partenaires = partenaireRepo.findAllById(request.getPartenaireIds());
            e.setPartenaires(partenaires);
        }
    }

    public EtablissementResponse toResponse(EtablissementCentre e) {
        return EtablissementResponse.builder()
                .id(e.getId())
                .nomFr(e.getNomFr())
                .nomAr(e.getNomAr())
                .code(e.getCode())
                .telephone(e.getTelephone())
                .fax(e.getFax())
                .adresse(e.getAdresse())
                .milieu(e.getMilieu())
                .typeLocal(e.getTypeLocal())
                .proprietecentre(e.getProprietecentre())
                .gererPar(e.getGererPar())
                .personneResponsableCentre(e.getPersonneResponsableCentre())
                .latitude(e.getLatitude())
                .longitude(e.getLongitude())
                .dateConstruction(e.getDateConstruction())
                .dateExploitation(e.getDateExploitation())
                .dateAchat(e.getDateAchat())
                .superficieTerrain(e.getSuperficieTerrain())
                .surfaceBatie(e.getSurfaceBatie())
                .superficieTotaleEtages(e.getSuperficieTotaleEtages())
                .nombreEtage(e.getNombreEtage())
                .etagesUtilises(e.getEtagesUtilises())
                .composant(e.getComposant())
                .capaciteAccueil(e.getCapaciteAccueil())
                .etatConstruction(e.getEtatConstruction())
                .observation(e.getObservation())
                .numerotitre(e.getNumerotitre())
                .loyer(e.getLoyer())
                .montantLoyer(e.getMontantLoyer())
                .paieLoyer(e.getPaieLoyer())
                .raccordementEauPotable(e.getRaccordementEauPotable())
                .raccordementElectricite(e.getRaccordementElectricite())
                .planSituation(e.getPlanSituation())
                .planArchitecture(e.getPlanArchitecture())
                .litige(e.getLitige())
                .raisonsConflit(e.getRaisonsConflit())
                .prixBatiment(e.getPrixBatiment())
                .autorise(e.getAutorise())
                .numeroAutorisation(e.getNumeroAutorisation())
                .utilisation(e.getUtilisation())
                .provinceId(e.getProvince() != null ? e.getProvince().getId() : null)
                .provinceNom(e.getProvince() != null ? e.getProvince().getNomFr() : null)
                .regionId(e.getRegion() != null ? e.getRegion().getId() : null)
                .regionNom(e.getRegion() != null ? e.getRegion().getNomFr() : null)
                .mappeCadastraleUrl(e.getMappeCadastraleUrl())
                .certificatProprieteUrl(e.getCertificatProprieteUrl())
                .planSituationUrl(e.getPlanSituationUrl())
                .planArchitectureUrl(e.getPlanArchitectureUrl())
                .photoUrl(e.getPhotoUrl())
                .programmes(e.getProgrammes() != null ? e.getProgrammes().stream()
                        .map(p -> ProgrammeResponse.builder()
                                .id(p.getId())
                                .nomFr(p.getNomFr())
                                .nomAr(p.getNomAr())
                                .build())
                        .collect(Collectors.toList()) : List.of())
                .prestationIds(e.getPrestations() != null ?
                        e.getPrestations().stream().map(Prestation::getId).collect(Collectors.toList()) :
                        List.of())
                .prestations(e.getPrestations() != null ?
                        e.getPrestations().stream()
                                .map(p -> PrestationResponse.builder()
                                        .id(p.getId())
                                        .nomFr(p.getNomFr())
                                        .nomAr(p.getNomAr())
                                        .programmeId(p.getProgramme() != null ? p.getProgramme().getId() : null)
                                        .build())
                                .collect(Collectors.toList()) :
                        List.of())
                .createdAt(e.getCreatedAt())
                .createdBy(e.getCreatedBy())
                .updatedAt(e.getUpdatedAt())
                .updatedBy(e.getUpdatedBy())
                .build();
    }
}