export class Maintenance {
  maintenance_id: number = 0;
  job_id: number = 0;
  job_status_id: number = 1;
  request_date_time: string = ""; // ISO format date string
  start_date_time?: string;  // ISO format date string
  estimated_end_date_time?: string; // ISO format date string
  worker_id?: number;
}
  