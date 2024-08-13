package dreamgarden.repositories;

import dreamgarden.entities.Maintainance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface MaintainanceRepository extends JpaRepository<Maintainance, Integer> {

}
