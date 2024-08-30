package dreamgarden.repositories;

import dreamgarden.entities.Job;
import dreamgarden.entities.Maintenance;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Integer> {

    List<Maintenance> findByJobId(Job jobId);
    @Query(value = """
        SELECT m.*
        FROM maintenance m 
        	JOIN job j ON m.job_id = j.job_id
        WHERE j.company_id = :companyId
        	AND m.job_status_id = :jobStatusId""", nativeQuery = true)
    List<Maintenance> findByCompanyIdAndJobStatusId(@Param("companyId") Integer companyId, @Param("jobStatusId") Integer jobStatusId);
    
    @Query(value = """
        SELECT m.* 
        FROM maintenance m 
        WHERE m.worker_id = :workerId 
            AND m.company_id = :companyId
            AND :jobDate BETWEEN m.start_date_time AND m.end_date_time""", nativeQuery = true)
    List<Maintenance> findByWorkerIdAndCompanyIdAndDateRange(@Param("workerId") Integer workerId, 
            @Param("companyId") Integer companyId, @Param("jobDate") Date jobDate);


    
}
