import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { AdminService } from 'src/app/services/admin/admin.service';
import { CompaniesService } from 'src/app/services/company/company.service';
import { Company } from 'src/app/models/company.model';
import { Service } from 'src/app/models/service.model';
import { ServicesService } from 'src/app/services/services/services.service';
import { JobsService } from 'src/app/services/job/job.service';
import { User } from 'src/app/models/user.model';
import { UserService } from 'src/app/services/user/user.service';
import { Job } from 'src/app/models/job.model';
import { Worker } from 'src/app/models/worker.model';
import { format } from 'date-fns';
import { JobStatus } from 'src/app/enums/job-status.enum';


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

  user: User = new User();
  worker: Worker = new Worker();
  pendingJobs: Array<Job> = [];
  acceptedJobs: Array<Job> = [];
  finishedJobs: Array<Job> = [];
  errorMsg: string = "";

  async ngOnInit() {
    try {
      this.user = JSON.parse(localStorage.getItem("user") || '{}');
      this.worker = await firstValueFrom(this.userService.getWorker(this.user.userId)) as Worker;
      const jobs = await firstValueFrom(this.jobService.getPendingByCompanyId(this.worker.companyId.companyId)) as Array<Job>;
      jobs.sort((b, a) => {
        return new Date(a.requestDateTime).getTime() - new Date(b.requestDateTime).getTime();
      });
      this.pendingJobs = jobs;   
    }
    catch (error: any) {
      this.errorMsg = error.error;
    }
    try {
      const inporgressJobs = await firstValueFrom(this.jobService.getByStatusAndWorker(this.user.userId, JobStatus.InProgress)) as Array<Job>;
      this.acceptedJobs = inporgressJobs;
    }
    catch (error: any) {
      this.errorMsg = error.error;
    }
    try {
      const acceptedJobs = await firstValueFrom(this.jobService.getByStatusAndWorker(this.user.userId, JobStatus.Accepted)) as Array<Job>;
      acceptedJobs.forEach(job => {
        this.acceptedJobs.push(job);
      });
    }
    catch (error: any) {
      this.errorMsg = error.error;
    }
    try {
      const finishedJobs = await firstValueFrom(this.jobService.getByStatusAndWorker(this.user.userId, JobStatus.Done)) as Array<Job>;
      finishedJobs.forEach(job => {
        this.finishedJobs.push(job);
      });
    }
    catch (error: any) {
      this.errorMsg = error.error;
    }
  }

  async accept(job: Job) {
    let data = {
      jobId: job.jobId,
      statusId: JobStatus.Accepted,
      userWorkerId: this.user.userId
    }
    await firstValueFrom(this.jobService.setStatus(data));
    const index = this.pendingJobs.indexOf(job);
    if (index > -1) {
      this.pendingJobs.splice(index, 1);
    }
    this.acceptedJobs.push(job);
  }

  async reject(job: Job) {
    if(job.rejectedDescription == "" || job.rejectedDescription == null) {
      alert("Obavezan je razlog odbijanja posla!");
      return;
    }
    let data = {
      jobId: job.jobId,
      statusId: JobStatus.Rejected,
      rejectionDescription: job.rejectedDescription,
      userWorkerId: this.user.userId
    }
    await firstValueFrom(this.jobService.setStatus(data));
    const index = this.pendingJobs.indexOf(job);
    if (index > -1) {
      this.pendingJobs.splice(index, 1); 
    }
  }

  async finish(job: Job) {
    let data = {
      jobId: job.jobId,
      statusId: JobStatus.Done,
      userWorkerId: this.user.userId
    }
    await firstValueFrom(this.jobService.setStatus(data));
    const index = this.acceptedJobs.indexOf(job);
    if (index > -1) {
      this.acceptedJobs.splice(index, 1);
    }
    this.finishedJobs.push(job);
  }
}
