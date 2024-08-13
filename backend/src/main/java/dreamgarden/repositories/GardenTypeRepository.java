package dreamgarden.repositories;

import dreamgarden.entities.GardenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * @author vamilutinovic
 */
@Repository
public interface GardenTypeRepository extends JpaRepository<GardenType, Integer> {

    Optional<GardenType> findByGardenTypeId(Integer gardenTypeId);

}
