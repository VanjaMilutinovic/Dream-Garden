import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { AdminService } from 'src/app/services/admin/admin.service';
import { CompaniesService } from 'src/app/services/company/company.service';
import { Company } from 'src/app/models/company.model';
import { Service } from 'src/app/models/service.model';
import { ServicesService } from 'src/app/services/services/services.service';
import { CompanyHoliday } from 'src/app/models/company-holiday.model';

@Component({
  selector: 'app-companies',
  templateUrl: './companies.component.html',
  styleUrls: ['./companies.component.css']
})
export class CompaniesComponent {

  constructor(private adminService: AdminService,
    private servicesService: ServicesService,
    private companyService: CompaniesService,
    private router: Router){}

  allCompanies :Array<Company> = [];
  viewCompanies :Array<Company> = [];
  searchTermName: string = '';
  searchTermAddress: string = '';
  sortDirectionName: string = 'rastuće'; // Default sorting direction for name
  sortDirectionAddress: string = 'rastuće'; // Default sorting direction for address
  errorMsg: string = '';
  currentCompany: Company = new Company();
  currenyCompanyFlag: boolean = false;
  addServiceFlag: boolean = false;
  service: Service = new Service();
  addHolidayFlag: boolean = false;
  holiday: CompanyHoliday = new CompanyHoliday();
  holidayStart: Date = new Date();
  holidayEnd: Date = new Date();

  async ngOnInit() {
    try {
      const c = await firstValueFrom(this.companyService.getAll()) as Array<Company>;
      this.allCompanies = c;
      this.viewCompanies = c;
    }
    catch(error: any){
      this.errorMsg = error.error;
      console.log(error);
    }
  }

  search(): void {
    this.viewCompanies = this.allCompanies.filter(company => {
      const matchesName = company.name.toLowerCase().includes(this.searchTermName.toLowerCase());
      const matchesAddress = company.address.toLowerCase().includes(this.searchTermAddress.toLowerCase());
      return matchesName && matchesAddress;
    });
  }

  sortByName(): void {
    const direction = this.sortDirectionName === 'rastuće' ? 1 : -1;
    this.viewCompanies = [...this.viewCompanies].sort((a, b) => 
      a.name.localeCompare(b.name) * direction
    );
    this.sortDirectionName = this.sortDirectionName === 'rastuće' ? 'opadajuće' : 'rastuće';
  }

  sortByAddress(): void {
    const direction = this.sortDirectionAddress === 'rastuće' ? 1 : -1;
    this.viewCompanies = [...this.viewCompanies].sort((a, b) => 
      a.address.localeCompare(b.address) * direction
    );
    this.sortDirectionAddress = this.sortDirectionAddress === 'rastuće' ? 'opadajuće' : 'rastuće';
  }

  async show(company: Company){
    company.holidayList = await firstValueFrom(this.companyService.getHolidays(company.companyId)) as Array<CompanyHoliday>;
    this.currentCompany = company;
    this.currenyCompanyFlag = true;
    this.service.companyId = company;
  }
  
  editUser(){ }

  showNewCompany(){
    this.router.navigate(['admin/companies/createCompany']);
  }

  async onSubmit() {
    try {
      let data = {
        serviceName: this.service.serviceName,
        companyId: this.service.companyId.companyId,
        price: this.service.price,
        serviceDescription: this.service.serviceDescription
      }
      const s = await firstValueFrom(this.servicesService.createService(data)) as Service;
      this.currentCompany.serviceList.push(s);
      this.service = new Service();
      this.service.companyId = this.currentCompany;
      this.addServiceFlag=false;
      const c = await firstValueFrom(this.companyService.getAll()) as Array<Company>;
      this.allCompanies = c;
    }
    catch(error: any){
      this.errorMsg = error.error;
      console.log(error);
    }
  }

  async onSubmitHoliday(){
    if (this.holidayStart == null || this.holidayEnd == null) {
      alert("Izaberite datum odmora!");
      return;
    }
    if (this.holidayEnd < this.holidayStart) {
      alert("Datum kraja odmora mora biti posle datuma početka odmora!");
      return;
    }
    try{
      const holiday = await firstValueFrom(this.companyService.createHoliday(
        this.currentCompany.companyId, new Date(this.holidayStart), new Date(this.holidayEnd))) as CompanyHoliday;
      this.currentCompany.holidayList.push(holiday);
    }
    catch(error: any){
      this.errorMsg = error.error;
      return;
    }
  }

}
