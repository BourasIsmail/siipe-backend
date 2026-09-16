package ma.entraide.siipe.repository;

import ma.entraide.siipe.entity.Prestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestationRepository extends JpaRepository<Prestation, Long> {
    List<Prestation> findByDeletedFalse();
    List<Prestation> findByProgrammeIdAndDeletedFalse(Long programmeId);
}
