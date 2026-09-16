package ma.entraide.siipe.repository;

import ma.entraide.siipe.entity.BesoinExprime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BesoinExprimeRepository extends JpaRepository<BesoinExprime, Long> {

    @Query("SELECT s FROM BesoinExprime s WHERE s.beneficiaire.id = :beneficiaireId AND s.deleted = false")
    List<BesoinExprime> findByBeneficiaireId(@Param("beneficiaireId") Long beneficiaireId);
}
