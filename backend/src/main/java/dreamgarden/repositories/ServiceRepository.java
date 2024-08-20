package dreamgarden.repositories;

import dreamgarden.entities.Company;
import dreamgarden.entities.Service;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {
    List<Service> findByCompanyId(Company companyId);
    List<Service> findByCompanyIdAndServiceName(Company companyId, String serviceName);
}
