package dreamgarden.repositories;

import dreamgarden.entities.User;
import dreamgarden.entities.UserType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author vamilutinovic
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByContactNumber(String contactNumber);
    List<User> findByUserTypeId(UserType userTypeId);
}
