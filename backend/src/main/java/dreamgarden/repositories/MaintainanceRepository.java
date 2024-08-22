package dreamgarden.repositories;

import dreamgarden.entities.Job;
import dreamgarden.entities.Maintainance;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface MaintainanceRepository extends JpaRepository<Maintainance, Integer> {

    List<Maintainance> findByJobId(Job jobId);
    
}
