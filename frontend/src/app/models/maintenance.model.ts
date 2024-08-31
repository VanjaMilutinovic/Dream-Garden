export class Maintenance {
  maintenanceId: number = 0;
  jobId: number = 0;
  jobStatusId: number = 1;
  requestDateTime: string = ""; // ISO format date string
  startDateTime?: string;  // ISO format date string
  estimatedEndDateTime?: string; // ISO format date string
  workerId?: number;
}
  