package ma.entraide.siipe.repository;

import ma.entraide.siipe.entity.DossierScolaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DossierScolaireRepository extends JpaRepository<DossierScolaire, Long> {

    @Query("SELECT s FROM DossierScolaire s WHERE s.beneficiaire.id = :beneficiaireId AND s.deleted = false")
    List<DossierScolaire> findByBeneficiaireId(@Param("beneficiaireId") Long beneficiaireId);}
