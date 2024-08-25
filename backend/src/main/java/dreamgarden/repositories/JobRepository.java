package dreamgarden.repositories;

import dreamgarden.entities.Job;
import dreamgarden.response.CountingStatisticResponse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
    
    @Query(value = """
        SELECT DATE_FORMAT(j.start_date_time, '%M %Y') AS variableName, COUNT(j.job_id) AS variableCount
        FROM job j
        WHERE j.worker_id = 3
        GROUP BY DATE_FORMAT(j.start_date_time, '%M %Y')
        ORDER BY MIN(j.start_date_time) DESC
        LIMIT 25
    """, nativeQuery = true)
    List<Object[]> findJobCountByWorkerAndMonth(@Param("workerId") Integer workerId);
    
    @Query(value = """
    	SELECT "24 hours",
            SUM(CASE WHEN j.request_date_time >= NOW() - INTERVAL 24 HOUR THEN 1 ELSE 0 END) AS `variableCount`
        FROM job j
        UNION
        SELECT "7 days",
            SUM(CASE WHEN j.request_date_time >= NOW() - INTERVAL 7 DAY THEN 1 ELSE 0 END) AS `variableCount`
        FROM job j
        UNION
        SELECT "30 days",
            SUM(CASE WHEN j.request_date_time >= NOW() - INTERVAL 30 DAY THEN 1 ELSE 0 END) AS `variableCount`
        FROM job j
    """, nativeQuery = true)
    List<Object[]> findJobCountsByRequestTime();
    
    @Query(value = """
        SELECT CONCAT(w.name, ' ', w.lastname) AS variableName, COUNT(j.job_id) AS variableCount
        FROM job j
            JOIN user w ON j.worker_id = w.user_id
        WHERE j.company_id = :companyId
        GROUP BY j.worker_id
    """, nativeQuery = true)
    List<Object[]> findJobCountsByCompany(@Param("companyId") Integer companyId);

    @Query(value = """
        SELECT DAYNAME(j.start_date_time) AS variableName, COUNT(j.job_id) AS variableCount
        FROM job j
        WHERE j.start_date_time >= DATE_SUB(CURDATE(), INTERVAL 24 MONTH)
        GROUP BY DAYOFWEEK(j.start_date_time)
        ORDER BY DAYOFWEEK(j.start_date_time)
    """, nativeQuery = true)
    List<Object[]> findJobCountsByDay();

 
}
