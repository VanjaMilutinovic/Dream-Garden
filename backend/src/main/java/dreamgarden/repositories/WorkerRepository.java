package dreamgarden.repositories;

import dreamgarden.entities.User;
import dreamgarden.entities.Worker;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface WorkerRepository extends JpaRepository<Worker, Integer> {

    Optional<Worker> findByUserId(User userId);
    
}
