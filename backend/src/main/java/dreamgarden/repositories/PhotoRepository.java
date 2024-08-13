package dreamgarden.repositories;

import dreamgarden.entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author vamilutinovic
 */
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    
    Optional<Photo> findByPath(String path);
    
}
