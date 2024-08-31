export class Job {
  jobId: number = 0;
  userId: number = 0;
  companyId: number = 0;
  workerId: number = 0;
  jobStatusId: number = 0;
  requestDateTime: string = ""; // ISO format date string
  startDateTime?: string;  // ISO format date string
  endDateTime?: string;    // ISO format date string
  gardenSize: number = 0;
  gardenTypeId: number = 1;
  description?: string;
  rejectedDescription?: string;
}
  