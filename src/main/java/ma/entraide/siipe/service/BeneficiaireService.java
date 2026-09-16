package ma.entraide.siipe.service;

import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.dto.request.*;
import ma.entraide.siipe.dto.response.*;
import ma.entraide.siipe.entity.*;
import ma.entraide.siipe.enums.Sexe;
import ma.entraide.siipe.enums.SituationDifficulte;
import ma.entraide.siipe.exception.ResourceNotFoundException;
import ma.entraide.siipe.repository.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BeneficiaireService {

    private final BeneficiaireRepository beneficiaireRepo;
    private final EtablissementCentreRepository etablissementRepo;
    private final ProgrammeRepository programmeRepo;
    private final PrestationRepository prestationRepo;
    private final UserRepository userRepo;
    private final SituationMedicaleRepository situationMedicaleRepo;
    private final SituationSocialeRepository situationSocialeRepo;
    private final SituationJudiciaireRepository situationJudiciaireRepo;
    private final DossierScolaireRepository dossierScolaireRepo;
    private final AccompagnementRepository accompagnementRepo;
    private final BesoinExprimeRepository besoinExprimeRepo;
    private final PrestationBeneficiaireRepository prestationBeneficiaireRepo;
    private final FileStorageService fileStorageService;

    public List<BeneficiaireResponse> getAll() {
        return beneficiaireRepo.findByDeletedFalse().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public List<BeneficiaireResponse> getByProvince(Long provinceId) {
        return beneficiaireRepo.findByEtablissementCentreProvinceIdAndDeletedFalse(provinceId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public List<BeneficiaireResponse> search(
            String nom, String prenom, String cin,
            Sexe sexe, SituationDifficulte situationDifficulte,
            LocalDate dateNaissanceFrom, LocalDate dateNaissanceTo,
            LocalDate dateEntreeFrom, LocalDate dateEntreeTo,
            Long etablissementId, Long provinceId, String typeHandicap) {

        Specification<Beneficiaire> spec = BeneficiaireSpecification.search(
                nom, prenom, cin, sexe, situationDifficulte,
                dateNaissanceFrom, dateNaissanceTo,
                dateEntreeFrom, dateEntreeTo,
                etablissementId, provinceId, typeHandicap);
        return beneficiaireRepo.findAll(spec).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public BeneficiaireResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Transactional
    public BeneficiaireResponse create(BeneficiaireRequest request) {
        Beneficiaire b = new Beneficiaire();
        mapRequestToEntity(request, b);
        return toResponse(beneficiaireRepo.save(b));
    }

    @Transactional
    public BeneficiaireResponse update(Long id, BeneficiaireRequest request) {
        Beneficiaire b = findById(id);
        mapRequestToEntity(request, b);
        return toResponse(beneficiaireRepo.save(b));
    }

    @Transactional
    public void delete(Long id) {
        Beneficiaire b = findById(id);
        b.setDeleted(true);
        beneficiaireRepo.save(b);
    }

    @Transactional
    public BeneficiaireResponse uploadPhoto(Long id, MultipartFile file) {
        Beneficiaire b = findById(id);
        String url = fileStorageService.storeFile(file, "beneficiaires/" + id);
        b.setPhotoUrl(url);
        return toResponse(beneficiaireRepo.save(b));
    }

    // ---- Sub-situation methods ----

    @Transactional
    public SituationMedicaleResponse addSituationMedicale(Long beneficiaireId, SituationMedicaleRequest req) {
        Beneficiaire b = findById(beneficiaireId);
        SituationMedicale s = SituationMedicale.builder()
                .beneficiaire(b)
                .situationMedicale(req.getSituationMedicale())
                .historiqueMedicale(req.getHistoriqueMedicale())
                .medecin(req.getMedecin())
                .utilisationMedicament(req.getUtilisationMedicament())
                .date(req.getDate())
                .observation(req.getObservation())
                .build();
        return toSituationMedicaleResponse(situationMedicaleRepo.save(s));
    }

    @Transactional
    public SituationMedicaleResponse uploadCertificatMedical(Long id, MultipartFile file) {
        SituationMedicale s = situationMedicaleRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Situation médicale non trouvée"));
        String url = fileStorageService.storeFile(file, "beneficiaires/medicales/" + id);
        s.setCertificatUrl(url);
        return toSituationMedicaleResponse(situationMedicaleRepo.save(s));
    }

    @Transactional
    public SituationSocialeResponse addSituationSociale(Long beneficiaireId, SituationSocialeRequest req) {
        Beneficiaire b = findById(beneficiaireId);
        SituationSociale s = SituationSociale.builder()
                .beneficiaire(b)
                .situationDifficulte(req.getSituationDifficulte())
                .typeViolence(req.getTypeViolence())
                .degre(req.getDegre())
                .sourceViolence(req.getSourceViolence())
                .lieuViolence(req.getLieuViolence())
                .dateViolence(req.getDateViolence())
                .violenceRepetee(req.getViolenceRepetee())
                .observation(req.getObservation())
                .build();
        return toSituationSocialeResponse(situationSocialeRepo.save(s));
    }

    @Transactional
    public SituationJudiciaireResponse addSituationJudiciaire(Long beneficiaireId, SituationJudiciaireRequest req) {
        Beneficiaire b = findById(beneficiaireId);
        SituationJudiciaire s = SituationJudiciaire.builder()
                .beneficiaire(b)
                .peine(req.getPeine())
                .peinesCriminelles(req.getPeinesCriminelles())
                .duree(req.getDuree())
                .lieux(req.getLieux())
                .date(req.getDate())
                .description(req.getDescription())
                .build();
        return toSituationJudiciaireResponse(situationJudiciaireRepo.save(s));
    }

    @Transactional
    public SituationJudiciaireResponse uploadPieceJointeJudiciaire(Long id, MultipartFile file) {
        SituationJudiciaire s = situationJudiciaireRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Situation judiciaire non trouvée"));
        String url = fileStorageService.storeFile(file, "beneficiaires/judiciaires/" + id);
        s.setPieceJointeUrl(url);
        return toSituationJudiciaireResponse(situationJudiciaireRepo.save(s));
    }

    @Transactional
    public DossierScolaireResponse addDossierScolaire(Long beneficiaireId, DossierScolaireRequest req) {
        Beneficiaire b = findById(beneficiaireId);
        DossierScolaire d = DossierScolaire.builder()
                .beneficiaire(b)
                .type(req.getType())
                .date(req.getDate())
                .decisions(req.getDecisions())
                .build();
        return toDossierScolaireResponse(dossierScolaireRepo.save(d));
    }

    @Transactional
    public DossierScolaireResponse uploadDossierFile(Long id, MultipartFile file) {
        DossierScolaire d = dossierScolaireRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dossier non trouvé"));
        String url = fileStorageService.storeFile(file, "beneficiaires/dossiers/" + id);
        d.setPieceJointeUrl(url);
        return toDossierScolaireResponse(dossierScolaireRepo.save(d));
    }

    @Transactional
    public AccompagnementResponse addAccompagnement(Long beneficiaireId, AccompagnementRequest req) {
        Beneficiaire b = findById(beneficiaireId);
        Accompagnement a = Accompagnement.builder()
                .beneficiaire(b)
                .preciserAccompagnement(req.getPreciserAccompagnement())
                .nomAccompagnement(req.getNomAccompagnement())
                .prenomAccompagnement(req.getPrenomAccompagnement())
                .adresseAccompagnement(req.getAdresseAccompagnement())
                .telephoneAccompagnement(req.getTelephoneAccompagnement())
                .emailAccompagnement(req.getEmailAccompagnement())
                .build();
        return toAccompagnementResponse(accompagnementRepo.save(a));
    }

    @Transactional
    public BesoinExprimeResponse addBesoinExprime(Long beneficiaireId, BesoinExprimeRequest req) {
        Beneficiaire b = findById(beneficiaireId);
        BesoinExprime be = BesoinExprime.builder()
                .beneficiaire(b)
                .libelle(req.getLibelle())
                .description(req.getDescription())
                .build();
        if (req.getProgrammeId() != null) {
            be.setProgramme(programmeRepo.findById(req.getProgrammeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Programme non trouvé")));
        }
        if (req.getPrestationId() != null) {
            be.setPrestation(prestationRepo.findById(req.getPrestationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Prestation non trouvée")));
        }
        return toBesoinExprimeResponse(besoinExprimeRepo.save(be));
    }

    @Transactional
    public PrestationBeneficiaireResponse addPrestationBeneficiaire(Long beneficiaireId, PrestationBeneficiaireRequest req) {
        Beneficiaire b = findById(beneficiaireId);
        PrestationBeneficiaire p = PrestationBeneficiaire.builder()
                .beneficiaire(b)
                .orientation(req.getOrientation())
                .serviceInterne(req.getServiceInterne())
                .dateDebut(req.getDateDebut())
                .statutPrestation(req.getStatutPrestation())
                .descriptionPhysique(req.getDescriptionPhysique())
                .build();
        if (req.getEtablissementId() != null) {
            p.setEtablissement(etablissementRepo.findById(req.getEtablissementId())
                    .orElseThrow(() -> new ResourceNotFoundException("Établissement non trouvé")));
        }
        if (req.getProgrammeId() != null) {
            p.setProgramme(programmeRepo.findById(req.getProgrammeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Programme non trouvé")));
        }
        if (req.getPrestationId() != null) {
            p.setPrestation(prestationRepo.findById(req.getPrestationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Prestation non trouvée")));
        }
        return toPrestationBeneficiaireResponse(prestationBeneficiaireRepo.save(p));
    }

    @Transactional
    public PrestationBeneficiaireResponse uploadPrestationFile(Long id, MultipartFile file) {
        PrestationBeneficiaire p = prestationBeneficiaireRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prestation non trouvée"));
        String url = fileStorageService.storeFile(file, "beneficiaires/prestations/" + id);
        p.setPieceJointeUrl(url);
        return toPrestationBeneficiaireResponse(prestationBeneficiaireRepo.save(p));
    }

    // ---- Getters ----

    public List<SituationMedicaleResponse> getSituationsMedicales(Long id) {
        return situationMedicaleRepo.findByBeneficiaireId(id).stream()
                .map(this::toSituationMedicaleResponse).collect(Collectors.toList());
    }

    public List<SituationSocialeResponse> getSituationsSociales(Long id) {
        return situationSocialeRepo.findByBeneficiaireId(id).stream()
                .map(this::toSituationSocialeResponse).collect(Collectors.toList());
    }

    public List<SituationJudiciaireResponse> getSituationsJudiciaires(Long id) {
        return situationJudiciaireRepo.findByBeneficiaireId(id).stream()
                .map(this::toSituationJudiciaireResponse).collect(Collectors.toList());
    }

    public List<DossierScolaireResponse> getDossiersScolaires(Long id) {
        return dossierScolaireRepo.findByBeneficiaireId(id).stream()
                .map(this::toDossierScolaireResponse).collect(Collectors.toList());
    }

    public List<AccompagnementResponse> getAccompagnements(Long id) {
        return accompagnementRepo.findByBeneficiaireId(id).stream()
                .map(this::toAccompagnementResponse).collect(Collectors.toList());
    }

    public List<BesoinExprimeResponse> getBesoinsExprimes(Long id) {
        return besoinExprimeRepo.findByBeneficiaireId(id).stream()
                .map(this::toBesoinExprimeResponse).collect(Collectors.toList());
    }

    public List<PrestationBeneficiaireResponse> getPrestationsBeneficiaire(Long id) {
        return prestationBeneficiaireRepo.findByBeneficiaireId(id).stream()
                .map(this::toPrestationBeneficiaireResponse).collect(Collectors.toList());
    }

    // ---- Mappers ----

    private SituationMedicaleResponse toSituationMedicaleResponse(SituationMedicale s) {
        return SituationMedicaleResponse.builder()
                .id(s.getId())
                .situationMedicale(s.getSituationMedicale())
                .historiqueMedicale(s.getHistoriqueMedicale())
                .medecin(s.getMedecin())
                .utilisationMedicament(s.getUtilisationMedicament())
                .date(s.getDate())
                .certificatUrl(s.getCertificatUrl())
                .observation(s.getObservation())
                .build();
    }

    private SituationSocialeResponse toSituationSocialeResponse(SituationSociale s) {
        return SituationSocialeResponse.builder()
                .id(s.getId())
                .situationDifficulte(s.getSituationDifficulte())
                .typeViolence(s.getTypeViolence())
                .degre(s.getDegre())
                .sourceViolence(s.getSourceViolence())
                .lieuViolence(s.getLieuViolence())
                .dateViolence(s.getDateViolence())
                .violenceRepetee(s.getViolenceRepetee())
                .observation(s.getObservation())
                .build();
    }

    private SituationJudiciaireResponse toSituationJudiciaireResponse(SituationJudiciaire s) {
        return SituationJudiciaireResponse.builder()
                .id(s.getId())
                .peine(s.getPeine())
                .peinesCriminelles(s.getPeinesCriminelles())
                .duree(s.getDuree())
                .lieux(s.getLieux())
                .date(s.getDate())
                .pieceJointeUrl(s.getPieceJointeUrl())
                .description(s.getDescription())
                .build();
    }

    private DossierScolaireResponse toDossierScolaireResponse(DossierScolaire d) {
        return DossierScolaireResponse.builder()
                .id(d.getId())
                .type(d.getType())
                .date(d.getDate())
                .decisions(d.getDecisions())
                .pieceJointeUrl(d.getPieceJointeUrl())
                .build();
    }

    private AccompagnementResponse toAccompagnementResponse(Accompagnement a) {
        return AccompagnementResponse.builder()
                .id(a.getId())
                .preciserAccompagnement(a.getPreciserAccompagnement())
                .nomAccompagnement(a.getNomAccompagnement())
                .prenomAccompagnement(a.getPrenomAccompagnement())
                .adresseAccompagnement(a.getAdresseAccompagnement())
                .telephoneAccompagnement(a.getTelephoneAccompagnement())
                .emailAccompagnement(a.getEmailAccompagnement())
                .build();
    }

    private BesoinExprimeResponse toBesoinExprimeResponse(BesoinExprime be) {
        return BesoinExprimeResponse.builder()
                .id(be.getId())
                .libelle(be.getLibelle())
                .programmeId(be.getProgramme() != null ? be.getProgramme().getId() : null)
                .programmeNom(be.getProgramme() != null ? be.getProgramme().getNomFr() : null)
                .prestationId(be.getPrestation() != null ? be.getPrestation().getId() : null)
                .prestationNom(be.getPrestation() != null ? be.getPrestation().getNomFr() : null)
                .description(be.getDescription())
                .build();
    }

    private PrestationBeneficiaireResponse toPrestationBeneficiaireResponse(PrestationBeneficiaire p) {
        return PrestationBeneficiaireResponse.builder()
                .id(p.getId())
                .etablissementId(p.getEtablissement() != null ? p.getEtablissement().getId() : null)
                .etablissementNom(p.getEtablissement() != null ? p.getEtablissement().getNomFr() : null)
                .programmeId(p.getProgramme() != null ? p.getProgramme().getId() : null)
                .programmeNom(p.getProgramme() != null ? p.getProgramme().getNomFr() : null)
                .prestationId(p.getPrestation() != null ? p.getPrestation().getId() : null)
                .prestationNom(p.getPrestation() != null ? p.getPrestation().getNomFr() : null)
                .orientation(p.getOrientation())
                .serviceInterne(p.getServiceInterne())
                .dateDebut(p.getDateDebut())
                .statutPrestation(p.getStatutPrestation())
                .descriptionPhysique(p.getDescriptionPhysique())
                .pieceJointeUrl(p.getPieceJointeUrl())
                .build();
    }

    private Beneficiaire findById(Long id) {
        return beneficiaireRepo.findById(id)
                .filter(b -> !b.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Bénéficiaire non trouvé"));
    }

    private void mapRequestToEntity(BeneficiaireRequest req, Beneficiaire b) {
        b.setNom(req.getNom());
        b.setPrenom(req.getPrenom());
        b.setNomAr(req.getNomAr());
        b.setPrenomAr(req.getPrenomAr());
        b.setAlias(req.getAlias());
        b.setCin(req.getCin());
        b.setNumActeNaissance(req.getNumActeNaissance());
        b.setNumPasseport(req.getNumPasseport());
        b.setNumeroDossier(req.getNumeroDossier());
        b.setTypePieceIdentite(req.getTypePieceIdentite());
        b.setSexe(req.getSexe());
        b.setDateNaissance(req.getDateNaissance());
        b.setLieuNaissance(req.getLieuNaissance());
        b.setNationalite(req.getNationalite());
        b.setAdresse(req.getAdresse());
        b.setTelephone(req.getTelephone());
        b.setSituationDifficulte(req.getSituationDifficulte());
        b.setDateEntree(req.getDateEntree());
        b.setDateSortie(req.getDateSortie());
        b.setMotifSortie(req.getMotifSortie());
        b.setVisitesADomicile(req.getVisitesADomicile());
        b.setSituationScolaire(req.getSituationScolaire());
        b.setSituationFamiliale(req.getSituationFamiliale());
        b.setTemoignageFamille(req.getTemoignageFamille());
        b.setDescriptionPhysique(req.getDescriptionPhysique());
        b.setEtatSantePsychique(req.getEtatSantePsychique());
        b.setSituationProfessionnelle(req.getSituationProfessionnelle());
        b.setSourceRevenu(req.getSourceRevenu());
        b.setCouvertureSociale(req.getCouvertureSociale());
        b.setRevenuMensuel(req.getRevenuMensuel());
        b.setEtatComportement(req.getEtatComportement());
        b.setDescription(req.getDescription());
        b.setNomPere(req.getNomPere());
        b.setNomMere(req.getNomMere());
        b.setNomTuteur(req.getNomTuteur());
        b.setTelephoneParent(req.getTelephoneParent());
        b.setAdresseParent(req.getAdresseParent());

        if (req.getEtablissementCentreId() != null) {
            b.setEtablissementCentre(etablissementRepo.findById(req.getEtablissementCentreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Établissement non trouvé")));
        }
        if (req.getProgrammeId() != null) {
            b.setProgramme(programmeRepo.findById(req.getProgrammeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Programme non trouvé")));
        }
        if (req.getPrestationId() != null) {
            b.setPrestation(prestationRepo.findById(req.getPrestationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Prestation non trouvée")));
        }
        if (req.getAssistanteSocialeId() != null) {
            b.setAssistanteSociale(userRepo.findById(req.getAssistanteSocialeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assistante sociale non trouvée")));
        }
    }

    public BeneficiaireResponse toResponse(Beneficiaire b) {
        return BeneficiaireResponse.builder()
                .id(b.getId())
                .nom(b.getNom()).prenom(b.getPrenom())
                .nomAr(b.getNomAr()).prenomAr(b.getPrenomAr())
                .alias(b.getAlias()).cin(b.getCin())
                .numActeNaissance(b.getNumActeNaissance())
                .numPasseport(b.getNumPasseport())
                .numeroDossier(b.getNumeroDossier())
                .typePieceIdentite(b.getTypePieceIdentite())
                .sexe(b.getSexe())
                .dateNaissance(b.getDateNaissance())
                .lieuNaissance(b.getLieuNaissance())
                .nationalite(b.getNationalite())
                .adresse(b.getAdresse()).telephone(b.getTelephone())
                .situationDifficulte(b.getSituationDifficulte())
                .dateEntree(b.getDateEntree()).dateSortie(b.getDateSortie())
                .motifSortie(b.getMotifSortie())
                .visitesADomicile(b.getVisitesADomicile())
                .situationScolaire(b.getSituationScolaire())
                .situationFamiliale(b.getSituationFamiliale())
                .temoignageFamille(b.getTemoignageFamille())
                .descriptionPhysique(b.getDescriptionPhysique())
                .etatSantePsychique(b.getEtatSantePsychique())
                .situationProfessionnelle(b.getSituationProfessionnelle())
                .sourceRevenu(b.getSourceRevenu())
                .couvertureSociale(b.getCouvertureSociale())
                .revenuMensuel(b.getRevenuMensuel())
                .etatComportement(b.getEtatComportement())
                .description(b.getDescription())
                .nomPere(b.getNomPere()).nomMere(b.getNomMere())
                .nomTuteur(b.getNomTuteur())
                .telephoneParent(b.getTelephoneParent())
                .adresseParent(b.getAdresseParent())
                .photoUrl(b.getPhotoUrl())
                .etablissementCentreId(b.getEtablissementCentre() != null ? b.getEtablissementCentre().getId() : null)
                .etablissementCentreNom(b.getEtablissementCentre() != null ? b.getEtablissementCentre().getNomFr() : null)
                .programmeId(b.getProgramme() != null ? b.getProgramme().getId() : null)
                .programmeNom(b.getProgramme() != null ? b.getProgramme().getNomFr() : null)
                .prestationId(b.getPrestation() != null ? b.getPrestation().getId() : null)
                .prestationNom(b.getPrestation() != null ? b.getPrestation().getNomFr() : null)
                .assistanteSocialeNom(b.getAssistanteSociale() != null ? b.getAssistanteSociale().getFullName() : null)
                .provinceId(b.getEtablissementCentre() != null && b.getEtablissementCentre().getProvince() != null
                        ? b.getEtablissementCentre().getProvince().getId() : null)
                .provinceNom(b.getEtablissementCentre() != null && b.getEtablissementCentre().getProvince() != null
                        ? b.getEtablissementCentre().getProvince().getNomFr() : null)
                .createdAt(b.getCreatedAt()).createdBy(b.getCreatedBy())
                .updatedAt(b.getUpdatedAt()).updatedBy(b.getUpdatedBy())
                .build();
    }
}