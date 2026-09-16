package ma.entraide.siipe.repository;

import ma.entraide.siipe.entity.Beneficiaire;
import ma.entraide.siipe.enums.SituationDifficulte;
import ma.entraide.siipe.enums.Sexe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DashboardRepository {
}
