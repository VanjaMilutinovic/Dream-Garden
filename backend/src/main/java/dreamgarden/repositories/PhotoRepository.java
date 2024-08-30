package dreamgarden.repositories;

import dreamgarden.entities.Photo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author vamilutinovic
 */
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    
    Optional<Photo> findByPath(String path);
    
    @Query(value = """
        SELECT p.path
        FROM photo p
        WHERE p.path LIKE ('job%')
        ORDER BY p.photo_id DESC
        LIMIT :k""", nativeQuery = true)
    List<String> findTopKByPhotoPath(@Param("k") Integer k);
    
}
