package dreamgarden.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dreamgarden.entities.Company;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author vamilutinovic
 */
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findByName(String name);
    List<Company> findByContactPerson(String contactPerson);
    // Add more custom queries if needed
}