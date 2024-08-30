export class Job {
  job_id: number = 0;
  user_id: number = 0;
  company_id: number = 0;
  worker_id: number = 0;
  job_status_id: number = 0;
  request_date_time: string = ""; // ISO format date string
  start_date_time?: string;  // ISO format date string
  end_date_time?: string;    // ISO format date string
  garden_size: number = 0;
  garden_type_id: number = 1;
  description?: string;
  rejected_description?: string;
}
  