package ma.entraide.siipe.repository;

import ma.entraide.siipe.entity.Partenaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartenaireRepository extends JpaRepository<Partenaire, Long> {
    List<Partenaire> findByDeletedFalse();
    List<Partenaire> findByProvinceIdAndDeletedFalse(Long provinceId);
    List<Partenaire> findByRegionIdAndDeletedFalse(Long regionId);
}
