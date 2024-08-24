package dreamgarden.repositories;

import dreamgarden.entities.Job;
import dreamgarden.entities.Maintenance;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Integer> {

    List<Maintenance> findByJobId(Job jobId);
    
}
