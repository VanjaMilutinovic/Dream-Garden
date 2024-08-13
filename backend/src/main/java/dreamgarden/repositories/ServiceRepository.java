package dreamgarden.repositories;

import dreamgarden.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {
    
}
