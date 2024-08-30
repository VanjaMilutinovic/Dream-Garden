package dreamgarden.repositories;

import dreamgarden.entities.Company;
import dreamgarden.entities.User;
import dreamgarden.entities.Worker;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface WorkerRepository extends JpaRepository<Worker, Integer> {

    Optional<Worker> findByUserId(User userId);
    List<Worker> findByCompanyId(Company companyId);
    
}
