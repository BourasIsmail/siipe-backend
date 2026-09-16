package ma.entraide.siipe.repository;

import ma.entraide.siipe.entity.SituationSociale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SituationSocialeRepository extends JpaRepository<SituationSociale, Long> {
    @Query("SELECT s FROM SituationSociale s WHERE s.beneficiaire.id = :beneficiaireId AND s.deleted = false")
    List<SituationSociale> findByBeneficiaireId(@Param("beneficiaireId") Long beneficiaireId);
}