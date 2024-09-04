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
        WHERE j.user_id = :userId
            AND m.job_status_id = :jobStatusId""", nativeQuery = true)
    List<Maintenance> findByUserIdAndStatusId(@Param("userId") Integer userId, @Param("jobStatusId") Integer jobStatusId);
    
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
            JOIN job j ON m.job_id = j.job_id
        WHERE m.worker_id = :workerId 
            AND j.company_id = :companyId
            AND ((m.estimated_end_date_time is not null AND :jobDate BETWEEN m.start_date_time AND m.estimated_end_date_time) 
            OR (m.estimated_end_date_time is null AND :jobDate BETWEEN m.start_date_time AND DATE_ADD(m.start_date_time, INTERVAL 1 DAY)))""", nativeQuery = true)
    List<Maintenance> findByWorkerIdAndCompanyIdAndDateRange(@Param("workerId") Integer workerId, 
            @Param("companyId") Integer companyId, @Param("jobDate") Date jobDate);


    
}
