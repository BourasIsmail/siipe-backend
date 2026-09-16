package ma.entraide.siipe.repository;

import ma.entraide.siipe.entity.SituationMedicale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SituationMedicaleRepository extends JpaRepository<SituationMedicale, Long> {

    @Query("SELECT s FROM SituationMedicale s WHERE s.beneficiaire.id = :beneficiaireId AND s.deleted = false")
    List<SituationMedicale> findByBeneficiaireId(@Param("beneficiaireId") Long beneficiaireId);
}
