package ma.entraide.siipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DashboardResponse {

    // Global counts
    private long totalBeneficiaires;
    private long totalPersonnel;
    private long totalEtablissements;
    private long totalPartenaires;
    private long totalSubventions;
    private double totalMontantSubventions;

    // Beneficiaires breakdown
    private Map<String, Long> beneficiairesBySexe;
    private Map<String, Long> beneficiairesBySituationDifficulte;
    private Map<String, Long> beneficiairesByProvince;
    private Map<String, Long> beneficiairesByEtablissement;
    private Map<String, Long> beneficiairesByProgramme;
    private Map<String, Long> beneficiairesByMonth; // entries per month current year

    // Personnel breakdown
    private Map<String, Long> personnelByGrade;
    private Map<String, Long> personnelByFonction;
    private Map<String, Long> personnelBySexe;
    private Map<String, Long> personnelByEtablissement;

    // Etablissements breakdown
    private Map<String, Long> etablissementsByProvince;
    private Map<String, Long> etablissementsByTypeLocal;
    private Map<String, Long> etablissementsByMilieu;

    // Subventions breakdown
    private Map<String, Long> subventionsByStatut;
    private Map<String, Double> subventionsMontantByPartenaire;

    // Recent activity
    private List<BeneficiaireResponse> recentBeneficiaires;
    private List<PersonnelResponse> recentPersonnel;
}
