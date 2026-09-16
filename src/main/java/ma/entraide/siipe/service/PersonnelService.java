package ma.entraide.siipe.service;

import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.dto.request.PersonnelRequest;
import ma.entraide.siipe.dto.response.PersonnelResponse;
import ma.entraide.siipe.entity.*;
import ma.entraide.siipe.exception.BadRequestException;
import ma.entraide.siipe.exception.ResourceNotFoundException;
import ma.entraide.siipe.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonnelService {

    private final PersonnelRepository personnelRepo;
    private final EtablissementCentreRepository etablissementRepo;
    private final ProgrammeRepository programmeRepo;
    private final PrestationRepository prestationRepo;
    private final FileStorageService fileStorageService;

    public List<PersonnelResponse> getAll() {
        return personnelRepo.findByDeletedFalse().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public List<PersonnelResponse> getByProvince(Long provinceId) {
        return personnelRepo.findByProvinceId(provinceId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public List<PersonnelResponse> getByEtablissement(Long etablissementId) {
        return personnelRepo.findByEtablissementCentreIdAndDeletedFalse(etablissementId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public PersonnelResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Transactional
    public PersonnelResponse create(PersonnelRequest request) {
        if (personnelRepo.existsByMatricule(request.getMatricule())) {
            throw new BadRequestException("Un personnel avec ce matricule existe déjà");
        }
        Personnel p = new Personnel();
        mapRequestToEntity(request, p);
        return toResponse(personnelRepo.save(p));
    }

    @Transactional
    public PersonnelResponse update(Long id, PersonnelRequest request) {
        Personnel p = findById(id);
        if (!p.getMatricule().equals(request.getMatricule()) &&
                personnelRepo.existsByMatricule(request.getMatricule())) {
            throw new BadRequestException("Un personnel avec ce matricule existe déjà");
        }
        mapRequestToEntity(request, p);
        return toResponse(personnelRepo.save(p));
    }

    @Transactional
    public void delete(Long id) {
        Personnel p = findById(id);
        p.setDeleted(true);
        personnelRepo.save(p);
    }

    @Transactional
    public PersonnelResponse uploadPhoto(Long id, MultipartFile file) {
        Personnel p = findById(id);
        String url = fileStorageService.storeFile(file, "personnel/" + id);
        p.setPhotoUrl(url);
        return toResponse(personnelRepo.save(p));
    }

    private void mapRequestToEntity(PersonnelRequest req, Personnel p) {
        p.setNom(req.getNom());
        p.setPrenom(req.getPrenom());
        p.setCin(req.getCin());
        p.setSexe(req.getSexe());
        p.setMatricule(req.getMatricule());
        p.setNumCouvertureSociale(req.getNumCouvertureSociale());
        p.setEmail(req.getEmail());
        p.setTelephone(req.getTelephone());
        p.setDateNaissance(req.getDateNaissance());
        p.setLieuNaissance(req.getLieuNaissance());
        p.setSituationFamille(req.getSituationFamille());
        p.setNombreEnfant(req.getNombreEnfant());
        p.setSituationAdministratif(req.getSituationAdministratif());
        p.setDateRecrutement(req.getDateRecrutement());
        p.setGrade(req.getGrade());
        p.setNiveauScolaire(req.getNiveauScolaire());
        p.setDiplome(req.getDiplome());
        p.setSalaire(req.getSalaire());
        p.setCategorie(req.getCategorie());
        p.setFonction(req.getFonction());
        p.setPosteOccupe(req.getPosteOccupe());
        p.setRoleDescription(req.getRoleDescription());

        // Scores
        p.setOrganisation(req.getOrganisation());
        p.setActivite(req.getActivite());
        p.setSpecialisation(req.getSpecialisation());
        p.setInitiative(req.getInitiative());
        p.setAutonomie(req.getAutonomie());
        p.setAdaptationProfessionnelle(req.getAdaptationProfessionnelle());
        p.setRelationsTravail(req.getRelationsTravail());
        p.setTechniqueExecution(req.getTechniqueExecution());
        p.setCommunication(req.getCommunication());
        p.setToleranceStress(req.getToleranceStress());
        p.setAssiduitePointage(req.getAssiduitePointage());
        p.setServicePopulation(req.getServicePopulation());

        // Observations
        p.setObservation1(req.getObservation1());
        p.setObservation2(req.getObservation2());
        p.setObservation3(req.getObservation3());
        p.setObservation4(req.getObservation4());
        p.setObservation5(req.getObservation5());
        p.setObservation6(req.getObservation6());
        p.setObservation7(req.getObservation7());
        p.setObservation8(req.getObservation8());
        p.setObservation9(req.getObservation9());
        p.setObservation10(req.getObservation10());
        p.setObservation11(req.getObservation11());
        p.setObservation12(req.getObservation12());
        p.setObservation13(req.getObservation13());
        p.setObservation14(req.getObservation14());
        p.setObservation15(req.getObservation15());

        if (req.getEtablissementCentreId() != null) {
            EtablissementCentre e = etablissementRepo.findById(req.getEtablissementCentreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Établissement non trouvé"));
            p.setEtablissementCentre(e);
        }
        if (req.getProgrammeId() != null) {
            Programme prog = programmeRepo.findById(req.getProgrammeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Programme non trouvé"));
            p.setProgramme(prog);
        }
        if (req.getPrestationId() != null) {
            Prestation prest = prestationRepo.findById(req.getPrestationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Prestation non trouvée"));
            p.setPrestation(prest);
        }
    }

    private Personnel findById(Long id) {
        return personnelRepo.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Personnel non trouvé"));
    }

    public PersonnelResponse toResponse(Personnel p) {
        return PersonnelResponse.builder()
                .id(p.getId())
                .nom(p.getNom())
                .prenom(p.getPrenom())
                .cin(p.getCin())
                .sexe(p.getSexe())
                .matricule(p.getMatricule())
                .numCouvertureSociale(p.getNumCouvertureSociale())
                .email(p.getEmail())
                .telephone(p.getTelephone())
                .dateNaissance(p.getDateNaissance())
                .lieuNaissance(p.getLieuNaissance())
                .situationFamille(p.getSituationFamille())
                .nombreEnfant(p.getNombreEnfant())
                .situationAdministratif(p.getSituationAdministratif())
                .dateRecrutement(p.getDateRecrutement())
                .grade(p.getGrade())
                .niveauScolaire(p.getNiveauScolaire())
                .diplome(p.getDiplome())
                .salaire(p.getSalaire())
                .categorie(p.getCategorie())
                .fonction(p.getFonction())
                .posteOccupe(p.getPosteOccupe())
                .roleDescription(p.getRoleDescription())
                .photoUrl(p.getPhotoUrl())
                .etablissementCentreId(p.getEtablissementCentre() != null ? p.getEtablissementCentre().getId() : null)
                .etablissementCentreNom(p.getEtablissementCentre() != null ? p.getEtablissementCentre().getNomFr() : null)
                .programmeId(p.getProgramme() != null ? p.getProgramme().getId() : null)
                .programmeNom(p.getProgramme() != null ? p.getProgramme().getNomFr() : null)
                .prestationId(p.getPrestation() != null ? p.getPrestation().getId() : null)
                .prestationNom(p.getPrestation() != null ? p.getPrestation().getNomFr() : null)
                .provinceId(p.getEtablissementCentre() != null && p.getEtablissementCentre().getProvince() != null
                        ? p.getEtablissementCentre().getProvince().getId() : null)
                .provinceNom(p.getEtablissementCentre() != null && p.getEtablissementCentre().getProvince() != null
                        ? p.getEtablissementCentre().getProvince().getNomFr() : null)
                .organisation(p.getOrganisation())
                .activite(p.getActivite())
                .specialisation(p.getSpecialisation())
                .initiative(p.getInitiative())
                .autonomie(p.getAutonomie())
                .adaptationProfessionnelle(p.getAdaptationProfessionnelle())
                .relationsTravail(p.getRelationsTravail())
                .techniqueExecution(p.getTechniqueExecution())
                .communication(p.getCommunication())
                .toleranceStress(p.getToleranceStress())
                .assiduitePointage(p.getAssiduitePointage())
                .servicePopulation(p.getServicePopulation())
                .observation1(p.getObservation1())
                .observation2(p.getObservation2())
                .observation3(p.getObservation3())
                .observation4(p.getObservation4())
                .observation5(p.getObservation5())
                .observation6(p.getObservation6())
                .observation7(p.getObservation7())
                .observation8(p.getObservation8())
                .observation9(p.getObservation9())
                .observation10(p.getObservation10())
                .observation11(p.getObservation11())
                .observation12(p.getObservation12())
                .observation13(p.getObservation13())
                .observation14(p.getObservation14())
                .observation15(p.getObservation15())
                .createdAt(p.getCreatedAt())
                .createdBy(p.getCreatedBy())
                .updatedAt(p.getUpdatedAt())
                .updatedBy(p.getUpdatedBy())
                .build();
    }
}
