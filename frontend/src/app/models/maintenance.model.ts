import { JobStatus } from "./job-status.model";
import { Job } from "./job.model";
import { User } from "./user.model";

export class Maintenance {
  maintenanceId: number = 0;
  jobId: Job = new Job();
  jobStatusId: JobStatus = new JobStatus();
  requestDateTime: string = ""; // ISO format date string
  startDateTime?: string;  // ISO format date string
  estimatedEndDateTime?: string; // ISO format date string
  workerId?: User = new User();
}
  