package ma.entraide.siipe.repository;

import ma.entraide.siipe.entity.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, Long> {
    List<Personnel> findByDeletedFalse();
    List<Personnel> findByEtablissementCentreIdAndDeletedFalse(Long etablissementId);
    boolean existsByMatricule(String matricule);
    Optional<Personnel> findByMatriculeAndDeletedFalse(String matricule);

    @Query("SELECT p FROM Personnel p WHERE p.deleted = false AND p.etablissementCentre.province.id = :provinceId")
    List<Personnel> findByProvinceId(Long provinceId);

    @Query("SELECT p FROM Personnel p WHERE p.deleted = false AND p.etablissementCentre.region.id = :regionId")
    List<Personnel> findByRegionId(Long regionId);
}
