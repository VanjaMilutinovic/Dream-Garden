import { CompanyHoliday } from "./company-holiday.model";
import { Service } from "./service.model";

export class Company {
  companyId: number = 0;
  name: string = "";
  address: string = "";
  latitude?: number = 0;
  longitude?: number = 0;
  contactNumber: string = "";
  contactPerson: string = "";
  serviceList: Array<Service> = [];
  holidayList: Array<CompanyHoliday> = [];
}
  