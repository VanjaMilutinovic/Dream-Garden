package dreamgarden.repositories;

import dreamgarden.entities.JobReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface JobReviewRepository extends JpaRepository<JobReview, Integer> {

}
