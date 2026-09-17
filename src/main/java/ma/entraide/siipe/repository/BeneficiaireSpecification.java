package ma.entraide.siipe.repository;

import jakarta.persistence.criteria.Predicate;
import ma.entraide.siipe.entity.Beneficiaire;
import ma.entraide.siipe.enums.Sexe;
import ma.entraide.siipe.enums.SituationDifficulte;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BeneficiaireSpecification {

    public static Specification<Beneficiaire> search(
            String nom,
            String prenom,
            String cin,
            Sexe sexe,
            SituationDifficulte situationDifficulte,
            LocalDate dateNaissanceFrom,
            LocalDate dateNaissanceTo,
            LocalDate dateEntreeFrom,
            LocalDate dateEntreeTo,
            Long etablissementId,
            Long provinceId,
            Long regionId,
            String typeHandicap
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("deleted"), false));

            if (nom != null && !nom.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nom")), "%" + nom.toLowerCase() + "%"));
            }
            if (prenom != null && !prenom.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("prenom")), "%" + prenom.toLowerCase() + "%"));
            }
            if (cin != null && !cin.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("cin")), "%" + cin.toLowerCase() + "%"));
            }
            if (sexe != null) {
                predicates.add(cb.equal(root.get("sexe"), sexe));
            }
            if (situationDifficulte != null) {
                predicates.add(cb.equal(root.get("situationDifficulte"), situationDifficulte));
            }
            if (dateNaissanceFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dateNaissance"), dateNaissanceFrom));
            }
            if (dateNaissanceTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dateNaissance"), dateNaissanceTo));
            }
            if (dateEntreeFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dateEntree"), dateEntreeFrom));
            }
            if (dateEntreeTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dateEntree"), dateEntreeTo));
            }
            if (etablissementId != null) {
                predicates.add(cb.equal(root.get("etablissementCentre").get("id"), etablissementId));
            }
            if (provinceId != null) {
                predicates.add(cb.equal(root.get("etablissementCentre").get("province").get("id"), provinceId));
            }
            if (regionId != null) {
                predicates.add(cb.equal(root.get("etablissementCentre").get("region").get("id"), regionId));
            }

            query.orderBy(cb.desc(root.get("createdAt")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
