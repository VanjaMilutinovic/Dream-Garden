package dreamgarden.repositories;

import dreamgarden.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dreamgarden.entities.CompanyHoliday;
import java.util.Date;

import java.util.List;

@Repository
public interface CompanyHolidayRepository extends JpaRepository<CompanyHoliday, Integer> {
    List<CompanyHoliday> findByCompanyId(Company companyId);
    List<CompanyHoliday> findByStartDateTimeBetween(Date startDateTime, Date endDateTime);
}
