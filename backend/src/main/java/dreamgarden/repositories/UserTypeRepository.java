package dreamgarden.repositories;

import dreamgarden.entities.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author vamilutinovic
 */
@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Integer> {
    
    List<UserType> findByName(String name);

}
