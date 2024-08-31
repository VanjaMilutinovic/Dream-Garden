import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { AdminService } from 'src/app/services/admin/admin.service';
import { CompaniesService } from 'src/app/services/company/company.service';
import { Company } from 'src/app/models/company.model';

@Component({
  selector: 'app-companies',
  templateUrl: './companies.component.html',
  styleUrls: ['./companies.component.css']
})
export class CompaniesComponent {

  constructor(private adminService: AdminService,
    private companyService: CompaniesService,
    private router: Router){}

  allCompanies :Array<Company> = [];
  viewCompanies :Array<Company> = [];
  errorMsg: any;
  currentCompany: Company = new Company();
  currenyCompanyFlag: boolean = false;

  async ngOnInit() {
    try {
      const c = await firstValueFrom(this.companyService.getAll()) as Array<Company>;
      this.allCompanies = c;
      this.viewCompanies = c;
    }
    catch(error){
      this.errorMsg = error;
      console.log(error);
    }
  }

  show(company: Company){
    this.currentCompany = company;
    this.currenyCompanyFlag = true;
  }
  editUser(){ }

  createNew(){
    this.router.navigate(['admin/companies/createCompany']);
  }

}
