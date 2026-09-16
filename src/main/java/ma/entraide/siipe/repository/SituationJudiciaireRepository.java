package ma.entraide.siipe.repository;

import ma.entraide.siipe.entity.SituationJudiciaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SituationJudiciaireRepository extends JpaRepository<SituationJudiciaire, Long> {

    @Query("SELECT s FROM SituationJudiciaire s WHERE s.beneficiaire.id = :beneficiaireId AND s.deleted = false")
    List<SituationJudiciaire> findByBeneficiaireId(@Param("beneficiaireId") Long beneficiaireId);}
