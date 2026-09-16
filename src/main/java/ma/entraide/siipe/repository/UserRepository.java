package ma.entraide.siipe.repository;

import ma.entraide.siipe.entity.User;
import ma.entraide.siipe.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByResetPasswordToken(String token);
    boolean existsByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByProvinceId(Long provinceId);
}
