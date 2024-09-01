import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { UserType } from 'src/app/enums/user-type.enum';
import { Company } from 'src/app/models/company.model';
import { JobService } from 'src/app/models/job-service.model';
import { Job } from 'src/app/models/job.model';
import { User } from 'src/app/models/user.model';
import { CompaniesWithWorkers } from 'src/app/response/companies-with-workers';
import { CountingStatisticResponse } from 'src/app/response/counting-statistic-response';
import { CompaniesService } from 'src/app/services/company/company.service';
import { JobsService } from 'src/app/services/job/job.service';
import { PhotoService } from 'src/app/services/photo/photo.service';
import { StatisticService } from 'src/app/services/statistic/statistic.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-companies',
  templateUrl: './companies.component.html',
  styleUrls: ['./companies.component.css']
})
export class CompaniesComponent {

  constructor(private statisticService: StatisticService,
    private jobService: JobsService,
    private userService: UserService,
    private photoService: PhotoService,
    private companyService: CompaniesService,
    private router: Router){}

  errorMsg: any;
  companies :Array<CompaniesWithWorkers> = [];
  viewCompanies :Array<CompaniesWithWorkers> = [];
  searchTermName: string = '';
  searchTermAddress: string = '';
  sortDirectionName: string = 'rastuće'; // Default sorting direction for name
  sortDirectionAddress: string = 'rastuće'; // Default sorting direction for address

  async ngOnInit() {
    try {
      const companies = await firstValueFrom(this.companyService.getAllCompaniesWithWorkers()) as Array<CompaniesWithWorkers>;
      this.companies = companies;
      this.viewCompanies = companies;
    }
    catch(error){
      this.errorMsg = error;
      console.log(error);
    }
  }

  search(): void {
    this.viewCompanies = this.companies.filter(company => {
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

  viewCompany(company: CompaniesWithWorkers): void {
    localStorage.setItem('companyId', JSON.stringify(company.companyId));
    this.router.navigate(['/owner/company-view']);
  }

}
