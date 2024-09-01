import { Company } from "./company.model";

export class Service {
  serviceId: number = 0;
  companyId: Company = new Company();
  price: number = 0;
  serviceName: string = "";
  serviceDescription?: string;
}
  