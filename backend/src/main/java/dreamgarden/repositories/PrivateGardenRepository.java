package dreamgarden.repositories;

import dreamgarden.entities.PrivateGarden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface PrivateGardenRepository extends JpaRepository<PrivateGarden, Integer> {

}
