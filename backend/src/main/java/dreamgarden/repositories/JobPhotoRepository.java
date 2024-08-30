package dreamgarden.repositories;

import dreamgarden.entities.Job;
import dreamgarden.entities.JobPhoto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface JobPhotoRepository extends JpaRepository<JobPhoto, Integer> {
    
    List<JobPhoto> findByJobId(Job jobId);
    
}
