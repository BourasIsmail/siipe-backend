package ma.entraide.siipe.repository;

import ma.entraide.siipe.entity.PrestationBeneficiaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrestationBeneficiaireRepository extends JpaRepository<PrestationBeneficiaire, Long> {
    List<PrestationBeneficiaire> findByBeneficiaireIdAndDeletedFalse(Long beneficiaireId);
    @Query("SELECT p FROM PrestationBeneficiaire p WHERE p.beneficiaire.id = :beneficiaireId AND p.deleted = false")
    List<PrestationBeneficiaire> findByBeneficiaireId(@Param("beneficiaireId") Long beneficiaireId);
}