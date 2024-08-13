package dreamgarden.repositories;

import dreamgarden.entities.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface JobStatusRepository extends JpaRepository<JobStatus, Integer> {
    
}
