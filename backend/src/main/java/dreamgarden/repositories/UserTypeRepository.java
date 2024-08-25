package dreamgarden.repositories;

import dreamgarden.entities.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

/**
 * @author vamilutinovic
 */
@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Integer> {
    
    List<UserType> findByName(String name);
    @Query(value = """
        SELECT ut.name AS variableName, COALESCE(COUNT(u.user_id), 0) AS variableCount 
        FROM user_type ut LEFT JOIN user u ON u.user_type_id = ut.user_type_id
        GROUP BY ut.name""", nativeQuery = true)
    List<Object[]> findTypeStatistic();
    
}
