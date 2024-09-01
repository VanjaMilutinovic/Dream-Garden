import { Component } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { UserType } from 'src/app/enums/user-type.enum';
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
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {

  constructor(private statisticService: StatisticService,
              private jobService: JobsService,
              private userService: UserService,
              private photoService: PhotoService,
              private companyService: CompaniesService,
              public sanitizer: DomSanitizer){}
  // Statistics and gallery
  decoratedGardens ?:number;
  registeredOwners ?:number;
  registeredDecorators ?:number;
  job24Hours ?:number;
  job7Days ?:number;
  job30Days ?:number;
  images : string[] = [];
  // Companies
  companies :Array<CompaniesWithWorkers> = [];
  viewCompanies :Array<CompaniesWithWorkers> = [];
  searchTermName: string = '';
  searchTermAddress: string = '';
  sortDirectionName: string = 'rastuće'; // Default sorting direction for name
  sortDirectionAddress: string = 'rastuće'; // Default sorting direction for address
  // Error handling
  errorMsg: any;

  async ngOnInit() {
    try {
      localStorage.removeItem('user');
      const jobStatistic = await firstValueFrom(this.statisticService.getByRequestTime()) as Array<CountingStatisticResponse>;
      this.job24Hours = jobStatistic[0].variableCount;
      this.job7Days = jobStatistic[1].variableCount;
      this.job30Days = jobStatistic[2].variableCount;
      const job = await firstValueFrom(this.jobService.getAll()) as Array<Job>;
      this.decoratedGardens = job.length;
      const owners = await firstValueFrom(this.userService.getByUserTypeId(UserType.Owner)) as Array<User>;
      this.registeredOwners = owners.length;
      const decorators = await firstValueFrom(this.userService.getByUserTypeId(UserType.Decorator)) as Array<User>;
      this.registeredDecorators = decorators.length;
      const photos = await firstValueFrom(this.photoService.getTopK(3)) as string[];
      photos.forEach(photo => {
        this.images.push(photo);
      });
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
}
