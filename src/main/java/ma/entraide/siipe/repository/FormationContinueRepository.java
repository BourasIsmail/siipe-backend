package ma.entraide.siipe.repository;

import ma.entraide.siipe.entity.FormationContinue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FormationContinueRepository extends JpaRepository<FormationContinue, Long> {
    List<FormationContinue> findByDeletedFalse();
    List<FormationContinue> findByEtablissementCentreIdAndDeletedFalse(Long etablissementId);
    List<FormationContinue> findByEtablissementCentreRegionIdAndDeletedFalse(Long regionId);
}
