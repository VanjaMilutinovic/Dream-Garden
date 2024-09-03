import { Company } from "./company.model";
import { GardenType } from "./garden-type.model";
import { JobReview } from "./job-review.model";
import { JobStatus } from "./job-status.model";
import { PrivateGarden } from "./private-garden.model";
import { RestaurantGarden } from "./restaurant-garden.model";
import { Service } from "./service.model";
import { User } from "./user.model";

export class Job {
  jobId: number = 0;
  userId: User = new User();
  companyId: Company = new Company();
  workerId: User = new User();
  jobStatusId: JobStatus = new JobStatus();
  requestDateTime: string = ""; // ISO format date string
  startDateTime: string = "";  // ISO format date string
  endDateTime: string = "";    // ISO format date string
  gardenSize: number = 0;
  gardenTypeId: GardenType = new GardenType();
  description?: string;
  rejectedDescription?: string;
  canvas!: string;
  jobReview!: JobReview;
  services: Array<Service> = [];
  restaurantGarden!: RestaurantGarden;
  privateGarden!: PrivateGarden;
  //temp
  maintenanceDate!: string; // ISO format date string
  lastLessThanSixMonths!: boolean;
}
  