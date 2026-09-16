package ma.entraide.siipe.repository;

import ma.entraide.siipe.entity.Accompagnement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccompagnementRepository extends JpaRepository<Accompagnement, Long> {

    @Query("SELECT s FROM Accompagnement s WHERE s.beneficiaire.id = :beneficiaireId AND s.deleted = false")
    List<Accompagnement> findByBeneficiaireId(@Param("beneficiaireId") Long beneficiaireId);
}
