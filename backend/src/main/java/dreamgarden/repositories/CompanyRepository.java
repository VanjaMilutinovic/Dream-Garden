package dreamgarden.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dreamgarden.entities.Company;

import java.util.List;

/**
 *
 * @author vamilutinovic
 */
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    List<Company> findByName(String name);
    List<Company> findByContactPerson(String contactPerson);
    // Add more custom queries if needed
}