package dreamgarden.repositories;

import dreamgarden.entities.Job;
import dreamgarden.entities.JobService;
import dreamgarden.entities.Service;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface JobServiceRepository extends JpaRepository<JobService, Integer> {
    List<Service> findByJobId(Job jobId);
}
