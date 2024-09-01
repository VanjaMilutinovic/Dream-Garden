import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { AdminService } from 'src/app/services/admin/admin.service';
import { CompaniesService } from 'src/app/services/company/company.service';
import { Company } from 'src/app/models/company.model';
import { Service } from 'src/app/models/service.model';
import { ServicesService } from 'src/app/services/services/services.service';
import { UserService } from 'src/app/services/user/user.service';
import { User } from 'src/app/models/user.model';

@Component({
  selector: 'app-create-company',
  templateUrl: './create-company.component.html',
  styleUrls: ['./create-company.component.css']
})
export class CreateCompanyComponent {
  constructor(private adminService: AdminService,
    private userService: UserService,
    private servicesService: ServicesService,
    private companyService: CompaniesService,
    private router: Router){}

  company: Company = new Company();
  unemployedWorkers: Array<User> = [];
  selectedWorkers: Array<User> = [];
  errorMsg: string = "";
  holidayStart: Date = new Date();
  holidayEnd: Date = new Date();
  addedServices: Array<Service> = [];
  addServiceFlag: boolean = false;
  service: Service = new Service()

  async ngOnInit() {
    this.unemployedWorkers = await firstValueFrom(this.userService.getUnemployedWorkers());
  }
  
  checkData() {
    if (this.company.name == null || this.company.name == "") {
      alert("Unesite naziv kompanije!");
      return false;
    }
    if (this.company.address == null || this.company.address == "") {
      alert("Unesite adresu kompanije!");
      return false;
    }
    if (this.company.contactNumber == null || this.company.contactNumber == "") {
      alert("Unesite kontakt telefon kompanije!");
      return false;
    }
    if (this.company.contactPerson == null || this.company.contactPerson == "") {
      alert("Unesite kontakt osobu kompanije!");
      return false;
    }
    if (this.selectedWorkers.length < 2) {
      alert("Izaberite minimum 2 radnika za kompaniju!");
      return false;
    }
    if (this.holidayStart == null || this.holidayEnd == null) {
      alert("Izaberite datum odmora!");
      return false;
    }
    if (this.holidayEnd < this.holidayStart) {
      alert("Datum kraja odmora mora biti posle datuma početka odmora!");
      return false;
    }
    return true;
  }

  onWorkerSelected(userId: User, event: any) {
    if (event.target.checked) {
      this.selectedWorkers.push(userId);
    } else {
      const index = this.selectedWorkers.indexOf(userId);
      if (index > -1) {
        this.selectedWorkers.splice(index, 1);
      }
    }
  }

  async onSubmit() {
    if (!this.checkData()) {
      return;
    }
    let data = {
      name: this.company.name,
      address: this.company.address,
      latitude: this.company.latitude,
      longitude: this.company.longitude,
      contactNumber: this.company.contactNumber,
      contactPerson: this.company.contactPerson
    };
    try {
      const c = await firstValueFrom(this.companyService.create(data)) as Company;
      await firstValueFrom(this.companyService.createHoliday(c.companyId, new Date(this.holidayStart), new Date(this.holidayEnd)));
      this.selectedWorkers.forEach(async w => {
        await firstValueFrom(this.adminService.employWorker(w.userId, c.companyId));
      });
      this.addedServices.forEach(async s => {
        let data = {
          serviceName: s.serviceName,
          companyId: c.companyId,
          price: s.price,
          serviceDescription: s.serviceDescription
        }
        await firstValueFrom(this.servicesService.createService(data));
      });
    } catch (error: any) {
      this.errorMsg = error.error;
      return;
    }

    this.router.navigate(['/admin/companies']);
  }

  async onSubmitService() {
    this.addedServices.push(this.service);
    this.service = new Service();
    this.addServiceFlag=false;
  }
}
