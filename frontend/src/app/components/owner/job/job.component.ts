import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { AdminService } from 'src/app/services/admin/admin.service';
import { CompaniesService } from 'src/app/services/company/company.service';
import { ServicesService } from 'src/app/services/services/services.service';
import { JobsService } from 'src/app/services/job/job.service';
import { User } from 'src/app/models/user.model';
import { UserService } from 'src/app/services/user/user.service';
import { Job } from 'src/app/models/job.model';
import { JobStatus } from 'src/app/enums/job-status.enum';
import { Company } from 'src/app/models/company.model';
import { JobReview } from 'src/app/models/job-review.model';
import { Service } from 'src/app/models/service.model';

@Component({
  selector: 'app-job',
  templateUrl: './job.component.html',
  styleUrls: ['./job.component.css']
})
export class JobComponent {

  constructor(private adminService: AdminService,
    private servicesService: ServicesService,
    private companyService: CompaniesService,
    private jobService: JobsService,
    private userService: UserService,
    private router: Router){}

  currentJobs: Array<Job> = [];
  finishedJobs: Array<Job> = [];
  user: User = new User();
  errorMsg: string = "";
  showPopup = false;
  reviewComment: string = '';
  reviewGrade: number | null = null;
  selectedJobId: number | null = null;
  selectedCanvas: string | null = null;

  async ngOnInit() {
    try {
      this.user = JSON.parse(localStorage.getItem("user") || '{}');
      const inporgressJobs = await firstValueFrom(this.jobService.getByStatusAndUser(this.user.userId, JobStatus.InProgress)) as Array<Job>;
      inporgressJobs.forEach(job => {this.currentJobs.push(job);})  
    }
    catch (error: any) {
      this.errorMsg = error.error;
    }
    try {
      const pendingJobs = await firstValueFrom(this.jobService.getByStatusAndUser(this.user.userId, JobStatus.Pending)) as Array<Job>;
      pendingJobs.forEach(job => {
        this.currentJobs.push(job);
      });
    }
    catch (error: any) {
      this.errorMsg = error.error;
    }
    try {
      const acceptedJobs = await firstValueFrom(this.jobService.getByStatusAndUser(this.user.userId, JobStatus.Accepted)) as Array<Job>;
      acceptedJobs.forEach(job => {
        this.currentJobs.push(job);
      });
    }
    catch (error: any) {
      this.errorMsg = error.error;
    }
    try {
      const finishedJobs = await firstValueFrom(this.jobService.getByStatusAndUser(this.user.userId, JobStatus.Done)) as Array<Job>;
      finishedJobs.forEach(job => {
        this.finishedJobs.push(job);
      });
      this.finishedJobs.sort((b, a) => {
        return new Date(a.endDateTime).getTime() - new Date(b.endDateTime).getTime();
      });
    }
    catch (error: any) {
      this.errorMsg = error.error;
    }
    this.getReviews();
    this.getServices();
  }

  async getReviews(){
    this.finishedJobs.forEach(async job => {      
      const review = await firstValueFrom(this.jobService.getreviewByJob(job.jobId)) as JobReview;
      job.jobReview = review;
    });
  }

  async getServices(){
    this.currentJobs.forEach(async job => {
      const services = await firstValueFrom(this.jobService.getServicesByJob(job.jobId)) as Array<Service>;
      job.services = services;
    });
  }

  isCancelable(jobStartDateTime: string): boolean {
    const currentDateTime = new Date();
    const startDateTime = new Date(jobStartDateTime);
    const timeDifference = startDateTime.getTime() - currentDateTime.getTime();
    
    // Check if the difference is greater than 1 day (24 hours)
    return timeDifference > (24 * 60 * 60 * 1000);
  }

  async cancel(job: Job) {
    try {
      await firstValueFrom(this.jobService.cancelJob(job.jobId, this.user.userId));
      this.currentJobs = this.currentJobs.filter(j => j !== job);
    } catch (error: any) {
      this.errorMsg = error.error;
    }
  }

  openPopup(jobId: number): void {
    this.selectedJobId = jobId;
    this.showPopup = true;
  }

  closePopup(): void {
    this.showPopup = false;
    this.selectedJobId = null;
    this.reviewComment = '';
    this.reviewGrade = null;
  }

  showCanvas(canvas: string) {
    this.selectedCanvas = canvas;
  }

  async submitReview(job: Job) {
    if (this.selectedJobId !== null && this.reviewComment && this.reviewGrade !== null) {
      let data = {
        jobId: this.selectedJobId,
        comment: this.reviewComment,
        grade: this.reviewGrade
      };
      const r = await firstValueFrom(this.jobService.addReview(data)) as JobReview;
      job.jobReview = r;
      this.closePopup();
    } else {
      // Handle validation errors if necessary
      console.error('Review form is not complete.');
    }
  }
}
