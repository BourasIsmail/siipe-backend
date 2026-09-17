package ma.entraide.siipe.repository;

import ma.entraide.siipe.entity.Subvention;
import ma.entraide.siipe.enums.StatutSubvention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubventionRepository extends JpaRepository<Subvention, Long> {
    List<Subvention> findByDeletedFalse();
    List<Subvention> findByEtablissementIdAndDeletedFalse(Long etablissementId);
    List<Subvention> findByEtablissementRegionIdAndDeletedFalse(Long regionId);
    List<Subvention> findByPartenaireIdAndDeletedFalse(Long partenaireId);
    List<Subvention> findByStatutAndDeletedFalse(StatutSubvention statut);
}
