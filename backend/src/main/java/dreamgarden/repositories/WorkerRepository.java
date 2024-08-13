package dreamgarden.repositories;

import dreamgarden.entities.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface WorkerRepository extends JpaRepository<Worker, Integer> {

    Worker findByUserId(Integer userId);
    
}
