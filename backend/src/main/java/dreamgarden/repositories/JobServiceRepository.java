package dreamgarden.repositories;

import dreamgarden.entities.JobService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface JobServiceRepository extends JpaRepository<JobService, Integer> {

}
