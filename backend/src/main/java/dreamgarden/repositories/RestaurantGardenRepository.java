package dreamgarden.repositories;

import dreamgarden.entities.RestaurantGarden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface RestaurantGardenRepository extends JpaRepository<RestaurantGarden, Integer> {

}
