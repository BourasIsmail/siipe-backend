package ma.entraide.siipe.repository;

import ma.entraide.siipe.entity.PosteProfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PosteProfilRepository extends JpaRepository<PosteProfil, Long> {
    List<PosteProfil> findByDeletedFalse();
    List<PosteProfil> findByEtablissementCentreIdAndDeletedFalse(Long etablissementId);
}
