package dreamgarden.repositories;

import dreamgarden.entities.UserStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, Integer> {
    List<UserStatus> findByStatus(String status);
}
