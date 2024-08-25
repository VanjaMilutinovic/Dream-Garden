package dreamgarden.repositories;

import dreamgarden.entities.JobStatus;
import dreamgarden.response.CountingStatisticResponse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author vamilutinovic
 */
@Repository
public interface JobStatusRepository extends JpaRepository<JobStatus, Integer> {
 
    @Query(value = """
        SELECT js.status AS variableName, COALESCE(COUNT(j.job_id), 0) AS variableCount 
        FROM job_status js LEFT JOIN job j ON j.job_status_id = js.job_status_id
        GROUP BY js.status""", nativeQuery = true)
    public List<Object[]> findStatusStatistic();

}
