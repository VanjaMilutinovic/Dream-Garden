import { Component, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { AdminService } from 'src/app/services/admin/admin.service';
import { CompaniesService } from 'src/app/services/company/company.service';
import { Company } from 'src/app/models/company.model';
import { Service } from 'src/app/models/service.model';
import { ServicesService } from 'src/app/services/services/services.service';
import { mapOptions } from '../../util/mapOptions';
import { CompanyHoliday } from 'src/app/models/company-holiday.model';
import { User } from 'src/app/models/user.model';
import { UserService } from 'src/app/services/user/user.service';
import { JobsService } from 'src/app/services/job/job.service';
import { JobReview } from 'src/app/models/job-review.model';
import { GardenType } from 'src/app/enums/garden-type.enum';
import { Job } from 'src/app/models/job.model';
import { CanvasComponent } from '../../test/canvas/canvas.component';


@Component({
  selector: 'app-company-view',
  templateUrl: './company-view.component.html',
  styleUrls: ['./company-view.component.css']
})
export class CompanyViewComponent {
  
  constructor(private adminService: AdminService,
    private servicesService: ServicesService,
    private userService: UserService,
    private companyService: CompaniesService,
    private jobService: JobsService,
    private router: Router){}

  mapOptions: google.maps.MapOptions = mapOptions
  pin !: any;
  center: google.maps.LatLngLiteral = {
    lat: 44.794358,
    lng: 20.4516521,
  };

  currentCompany: Company = new Company();
  currentUser: User = new User();
  reviews: Array<JobReview> = [];
  errorMsg: string = '';
  errorMsgJob: string = '';
  requestJobFlag: boolean = false;
  step: number = 1;
  now = new Date();
  reqDate: Date = this.now;
  reqType: number = 1;
  reqDescription: string = '';
  reqSize: number = 0;
  reqPrivate: { poolSize: number, numberOfPools: number, grassSize: number, pavedSize: number} = 
    {poolSize: 0, numberOfPools: 0, grassSize: 0, pavedSize: 0};
  reqRestaurant: { fountainSize: number, numberOfFountains: number, grassSize: number, numberOfTables: number,  numberOfSeats: number } = 
    {fountainSize: 0, numberOfFountains: 0, grassSize: 0, numberOfTables: 0,  numberOfSeats: 0};
  checkedServices: Array<number> = [];
  canvasFlag=false;


  async ngOnInit() {
    let companyId = JSON.parse(localStorage.getItem('companyId') || '{}');
    this.currentCompany = await firstValueFrom(this.companyService.getCompany(companyId)) as Company;
    this.currentUser = JSON.parse(localStorage.getItem('user') || '{}');
    this.currentCompany.holidayList = await firstValueFrom(this.companyService.getHolidays(this.currentCompany.companyId)) as Array<CompanyHoliday>;
    this.pin = { lat: this.currentCompany.latitude, lng: this.currentCompany.longitude }
    this.center = this.pin;
    try {
      this.reviews = await firstValueFrom(this.jobService.getReviews(this.currentCompany.companyId)) as Array<JobReview>;
    }
    catch (error: any) {
      this.errorMsg = error.error;
    }
    
  }

  moveMap(event: google.maps.MapMouseEvent) {
    console.log(event)
    if (event.latLng == null) return;
    this.pin ={lat: event.latLng.lat(),lng: event.latLng.lng()};
    this.currentCompany.latitude = event.latLng.lat();
    this.currentCompany.longitude = event.latLng.lng();
      this.center = event.latLng.toJSON();
  }

  onServiceChecked(event: any, serviceId: number): void {
    if (event.target.checked) {
      this.checkedServices.push(serviceId);
    } else {
      const index = this.checkedServices.indexOf(serviceId);
      if (index !== -1) {
        this.checkedServices.splice(index, 1);
      }
    }
  }

  checkStepOne() {
    if (this.reqDate == null || this.reqDate == undefined || this.reqDate == this.now) {
      alert("Unesite datum!");
      return;
    }
    if (this.reqSize == 0) {
      alert("Unesite povrsinu!");
      return;
    }
    this.step = 2;
  }

  @ViewChild(CanvasComponent) child!:CanvasComponent;
  async checkStepTwo() {
    if (this.reqType == 1) {
      if (this.reqPrivate.poolSize == 0 && this.reqPrivate.numberOfPools == 0 && this.reqPrivate.grassSize == 0 && this.reqPrivate.pavedSize == 0) {
        alert("Unesite sve podatke!");
        return;
      }
      if (this.reqPrivate.grassSize + this.reqPrivate.pavedSize + this.reqPrivate.poolSize != this.reqSize) {
        alert("Povrsina bazena, travnjaka i poplocane povrsine mora biti jednaka ukupnoj povrsini!");
        return;
      }
      if (this.reqPrivate.poolSize != 0 && this.reqPrivate.numberOfPools == 0) {
        alert("Unesite broj bazena!");
        return;
      }
    }
    else {
      if (this.reqRestaurant.fountainSize == 0 && this.reqRestaurant.numberOfFountains == 0 && this.reqRestaurant.grassSize == 0 && this.reqRestaurant.numberOfTables == 0 && this.reqRestaurant.numberOfSeats == 0) {
        alert("Unesite sve podatke!");
        return;
      }
      if (this.reqRestaurant.grassSize + this.reqRestaurant.fountainSize != this.reqSize) {
        alert("Povrsina travnjaka i fontana mora biti jednaka ukupnoj povrsini!");
        return;
      }
      if (this.reqRestaurant.fountainSize != 0 && this.reqRestaurant.numberOfFountains == 0) {
        alert("Unesite broj fontana!");
        return;
      }
    }
    if (this.checkedServices.length == 0) {
      alert("Odaberite usluge!");
      return;
    }
    if (this.child && this.child.getCanvasString() == null) 
      return;
    let data = {
      companyId: this.currentCompany.companyId,
      userId: this.currentUser.userId,
      startDateTime: this.reqDate,
      gardenSize: this.reqSize,      
      description: this.reqDescription,
      gardenTypeId: this.reqType,
      privateGarden: this.reqType==1 ? this.reqPrivate : null,
      restaurantGarden: this.reqType==2 ? this.reqRestaurant : null,
      canvas: this.child ? this.child.getCanvasString() : null 
    }
    console.log(data);
    try {
      const job = await firstValueFrom(this.jobService.createJob(data)) as Job;
      await firstValueFrom(this.jobService.addServices(job.jobId, this.checkedServices));
      alert("Zahtev je poslat!");
      this.errorMsgJob = '';
    }
    catch (error: any) {
      this.errorMsgJob = error.error;
      this.step = 1;
      return;
    } 
    this.step = 1;
    this.requestJobFlag = false;
  }
}
