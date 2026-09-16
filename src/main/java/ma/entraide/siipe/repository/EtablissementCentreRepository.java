package ma.entraide.siipe.repository;

import ma.entraide.siipe.entity.EtablissementCentre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtablissementCentreRepository extends JpaRepository<EtablissementCentre, Long> {
    List<EtablissementCentre> findByDeletedFalse();
    List<EtablissementCentre> findByProvinceIdAndDeletedFalse(Long provinceId);
    List<EtablissementCentre> findByRegionIdAndDeletedFalse(Long regionId);

    @Query("SELECT e FROM EtablissementCentre e WHERE e.deleted = false AND e.latitude IS NOT NULL AND e.longitude IS NOT NULL")
    List<EtablissementCentre> findAllWithCoordinates();

    @Query("SELECT e FROM EtablissementCentre e WHERE e.deleted = false AND e.province.id = :provinceId AND e.latitude IS NOT NULL AND e.longitude IS NOT NULL")
    List<EtablissementCentre> findByProvinceIdWithCoordinates(Long provinceId);
}
