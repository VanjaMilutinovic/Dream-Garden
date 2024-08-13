package dreamgarden.repositories;

import dreamgarden.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {

}
