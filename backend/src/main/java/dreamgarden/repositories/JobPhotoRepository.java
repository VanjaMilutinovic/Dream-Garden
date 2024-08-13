package dreamgarden.repositories;

import dreamgarden.entities.JobPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface JobPhotoRepository extends JpaRepository<JobPhoto, Integer> {
    
}
