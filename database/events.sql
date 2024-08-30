CREATE DEFINER=`root`@`localhost` EVENT `Job Status - Update Done` ON SCHEDULE EVERY 1 HOUR STARTS '2024-08-27 00:00:00' ENDS '2024-09-16 00:00:00' ON COMPLETION PRESERVE ENABLE COMMENT 'Automatic updating of job statuses based on start and end dates' DO UPDATE job j 
SET j.job_status_id = 5 
WHERE j.job_status_id = 4 
	AND j.end_date_time < NOW()

CREATE DEFINER=`root`@`localhost` EVENT `Job Status - Update In Progress` ON SCHEDULE EVERY 1 HOUR STARTS '2024-08-27 00:00:00' ENDS '2024-09-16 00:00:00' ON COMPLETION PRESERVE ENABLE COMMENT 'Automatic updating of job statuses based on start and end dates' DO UPDATE job j 
SET j.job_status_id = 4 
WHERE j.job_status_id = 3 
	AND j.start_date_time < NOW()

CREATE DEFINER=`root`@`localhost` EVENT `Maintenance Status - Update In Progress` ON SCHEDULE EVERY 1 HOUR STARTS '2024-08-27 00:00:00' ENDS '2024-09-16 00:00:00' ON COMPLETION PRESERVE ENABLE COMMENT 'Automatic updating of job statuses based on start and end dates' DO UPDATE maintenance m 
SET m.job_status_id = 4 
WHERE m.job_status_id = 3 
	AND m.start_date_time < NOW()

CREATE DEFINER=`root`@`localhost` EVENT `Maintenance Status - Update Done` ON SCHEDULE EVERY 1 HOUR STARTS '2024-08-27 00:00:00' ENDS '2024-09-16 00:00:00' ON COMPLETION PRESERVE ENABLE COMMENT 'Automatic updating of job statuses based on start and end dates' DO UPDATE maintenance m 
SET m.job_status_id = 5
WHERE m.job_status_id = 4 
	AND m.estimated_end_date_time < NOW()
