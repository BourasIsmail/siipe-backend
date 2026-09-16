package ma.entraide.siipe.service;

import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.dto.response.DashboardResponse;
import ma.entraide.siipe.entity.*;
import ma.entraide.siipe.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final BeneficiaireRepository beneficiaireRepo;
    private final PersonnelRepository personnelRepo;
    private final EtablissementCentreRepository etablissementRepo;
    private final PartenaireRepository partenaireRepo;
    private final SubventionRepository subventionRepo;
    private final BeneficiaireService beneficiaireService;
    private final PersonnelService personnelService;

    public DashboardResponse getGlobalDashboard() {
        List<Beneficiaire> beneficiaires = beneficiaireRepo.findByDeletedFalse();
        List<Personnel> personnel = personnelRepo.findByDeletedFalse();
        List<EtablissementCentre> etablissements = etablissementRepo.findByDeletedFalse();
        List<Partenaire> partenaires = partenaireRepo.findByDeletedFalse();
        List<ma.entraide.siipe.entity.Subvention> subventions = subventionRepo.findByDeletedFalse();

        return DashboardResponse.builder()
                // Counts
                .totalBeneficiaires(beneficiaires.size())
                .totalPersonnel(personnel.size())
                .totalEtablissements(etablissements.size())
                .totalPartenaires(partenaires.size())
                .totalSubventions(subventions.size())
                .totalMontantSubventions(subventions.stream()
                        .mapToDouble(s -> s.getMontant() != null ? s.getMontant() : 0).sum())

                // Beneficiaires breakdown
                .beneficiairesBySexe(groupBy(beneficiaires, b ->
                        b.getSexe() != null ? b.getSexe().name() : "NON_RENSEIGNE"))
                .beneficiairesBySituationDifficulte(groupBy(beneficiaires, b ->
                        b.getSituationDifficulte() != null ? b.getSituationDifficulte().name() : "NON_RENSEIGNE"))
                .beneficiairesByProvince(groupBy(beneficiaires, b ->
                        b.getEtablissementCentre() != null && b.getEtablissementCentre().getProvince() != null
                                ? b.getEtablissementCentre().getProvince().getNomFr() : "NON_RENSEIGNE"))
                .beneficiairesByEtablissement(groupBy(beneficiaires, b ->
                        b.getEtablissementCentre() != null ? b.getEtablissementCentre().getNomFr() : "NON_RENSEIGNE"))
                .beneficiairesByProgramme(groupBy(beneficiaires, b ->
                        b.getProgramme() != null ? b.getProgramme().getNomFr() : "NON_RENSEIGNE"))
                .beneficiairesByMonth(getBeneficiairesByMonth(beneficiaires))

                // Personnel breakdown
                .personnelByGrade(groupBy(personnel, p ->
                        p.getGrade() != null ? p.getGrade().name() : "NON_RENSEIGNE"))
                .personnelByFonction(groupBy(personnel, p ->
                        p.getFonction() != null ? p.getFonction().name() : "NON_RENSEIGNE"))
                .personnelBySexe(groupBy(personnel, p ->
                        p.getSexe() != null ? p.getSexe().name() : "NON_RENSEIGNE"))
                .personnelByEtablissement(groupBy(personnel, p ->
                        p.getEtablissementCentre() != null ? p.getEtablissementCentre().getNomFr() : "NON_RENSEIGNE"))

                // Etablissements breakdown
                .etablissementsByProvince(groupBy(etablissements, e ->
                        e.getProvince() != null ? e.getProvince().getNomFr() : "NON_RENSEIGNE"))
                .etablissementsByTypeLocal(groupBy(etablissements, e ->
                        e.getTypeLocal() != null ? e.getTypeLocal().name() : "NON_RENSEIGNE"))
                .etablissementsByMilieu(groupBy(etablissements, e ->
                        e.getMilieu() != null ? e.getMilieu().name() : "NON_RENSEIGNE"))

                // Subventions breakdown
                .subventionsByStatut(groupBy(subventions, s ->
                        s.getStatut() != null ? s.getStatut().name() : "NON_RENSEIGNE"))
                .subventionsMontantByPartenaire(subventions.stream()
                        .filter(s -> s.getPartenaire() != null)
                        .collect(Collectors.groupingBy(
                                s -> s.getPartenaire().getNomFr(),
                                Collectors.summingDouble(s -> s.getMontant() != null ? s.getMontant() : 0))))

                // Recent 5
                .recentBeneficiaires(beneficiaires.stream()
                        .sorted(Comparator.comparing(Beneficiaire::getCreatedAt,
                                Comparator.nullsLast(Comparator.reverseOrder())))
                        .limit(5)
                        .map(beneficiaireService::toResponse)
                        .collect(Collectors.toList()))
                .recentPersonnel(personnel.stream()
                        .sorted(Comparator.comparing(Personnel::getCreatedAt,
                                Comparator.nullsLast(Comparator.reverseOrder())))
                        .limit(5)
                        .map(personnelService::toResponse)
                        .collect(Collectors.toList()))

                .build();
    }

    public DashboardResponse getDashboardByProvince(Long provinceId) {
        List<Beneficiaire> beneficiaires = beneficiaireRepo
                .findByEtablissementCentreProvinceIdAndDeletedFalse(provinceId);
        List<Personnel> personnel = personnelRepo.findByProvinceId(provinceId);
        List<EtablissementCentre> etablissements = etablissementRepo
                .findByProvinceIdAndDeletedFalse(provinceId);

        return DashboardResponse.builder()
                .totalBeneficiaires(beneficiaires.size())
                .totalPersonnel(personnel.size())
                .totalEtablissements(etablissements.size())
                .totalPartenaires(0)
                .totalSubventions(0)
                .totalMontantSubventions(0)
                .beneficiairesBySexe(groupBy(beneficiaires, b ->
                        b.getSexe() != null ? b.getSexe().name() : "NON_RENSEIGNE"))
                .beneficiairesBySituationDifficulte(groupBy(beneficiaires, b ->
                        b.getSituationDifficulte() != null ? b.getSituationDifficulte().name() : "NON_RENSEIGNE"))
                .beneficiairesByEtablissement(groupBy(beneficiaires, b ->
                        b.getEtablissementCentre() != null ? b.getEtablissementCentre().getNomFr() : "NON_RENSEIGNE"))
                .beneficiairesByMonth(getBeneficiairesByMonth(beneficiaires))
                .personnelByGrade(groupBy(personnel, p ->
                        p.getGrade() != null ? p.getGrade().name() : "NON_RENSEIGNE"))
                .personnelByFonction(groupBy(personnel, p ->
                        p.getFonction() != null ? p.getFonction().name() : "NON_RENSEIGNE"))
                .recentBeneficiaires(beneficiaires.stream()
                        .sorted(Comparator.comparing(Beneficiaire::getCreatedAt,
                                Comparator.nullsLast(Comparator.reverseOrder())))
                        .limit(5)
                        .map(beneficiaireService::toResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    private <T> Map<String, Long> groupBy(List<T> list, java.util.function.Function<T, String> keyExtractor) {
        return list.stream().collect(Collectors.groupingBy(keyExtractor, Collectors.counting()));
    }

    private Map<String, Long> getBeneficiairesByMonth(List<Beneficiaire> beneficiaires) {
        int currentYear = java.time.LocalDate.now().getYear();
        Map<String, Long> result = new LinkedHashMap<>();
        String[] months = {"Jan", "Fév", "Mar", "Avr", "Mai", "Jun",
                "Jul", "Aoû", "Sep", "Oct", "Nov", "Déc"};
        for (String m : months) result.put(m, 0L);

        beneficiaires.stream()
                .filter(b -> b.getCreatedAt() != null &&
                        b.getCreatedAt().getYear() == currentYear)
                .forEach(b -> {
                    String month = months[b.getCreatedAt().getMonthValue() - 1];
                    result.merge(month, 1L, Long::sum);
                });
        return result;
    }
}
