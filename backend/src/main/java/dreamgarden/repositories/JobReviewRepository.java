package dreamgarden.repositories;

import dreamgarden.entities.Job;
import dreamgarden.entities.JobReview;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface JobReviewRepository extends JpaRepository<JobReview, Integer> {
    
    Optional<JobReview> findByJobId(Job jobId);

}
