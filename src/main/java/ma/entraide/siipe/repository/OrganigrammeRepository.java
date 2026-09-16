package ma.entraide.siipe.repository;

import ma.entraide.siipe.entity.Organigramme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrganigrammeRepository extends JpaRepository<Organigramme, Long> {
    List<Organigramme> findByDeletedFalse();
    List<Organigramme> findByEtablissementCentreIdAndDeletedFalse(Long etablissementId);
}
