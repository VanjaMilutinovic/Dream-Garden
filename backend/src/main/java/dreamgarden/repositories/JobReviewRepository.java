package dreamgarden.repositories;

import dreamgarden.entities.Job;
import dreamgarden.entities.JobReview;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface JobReviewRepository extends JpaRepository<JobReview, Integer> {
    
    Optional<JobReview> findByJobId(Job jobId);
    @Query(value = """
        SELECT AVG(jr.grade) 
        FROM Job_Review jr 
        JOIN Job j ON jr.job_id = j.job_id 
        WHERE j.company_id = :companyId""", nativeQuery = true)
    Double getRating(@Param("companyId") Integer companyId);
}
