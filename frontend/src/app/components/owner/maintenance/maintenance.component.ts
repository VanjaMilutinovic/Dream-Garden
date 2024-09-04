import { Component } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { JobStatus } from 'src/app/enums/job-status.enum';
import { Job } from 'src/app/models/job.model';
import { Maintenance } from 'src/app/models/maintenance.model';
import { User } from 'src/app/models/user.model';
import { Worker } from 'src/app/models/worker.model';
import { JobsService } from 'src/app/services/job/job.service';
import { MaintenanceService } from 'src/app/services/maintenance/maintenance.service';
import { UserService } from 'src/app/services/user/user.service';


@Component({
  selector: 'app-maintenance',
  templateUrl: './maintenance.component.html',
  styleUrls: ['./maintenance.component.css']
})
export class MaintenanceComponent {
  constructor(private jobService: JobsService,
    private maintenanceService: MaintenanceService,
    private userService: UserService){}

    finishedJobs: Array<Job> = [];
    activeMaintenance: Array<Maintenance> = [];
    user: User = new User();
    errorMsg: string = "";
    selectedStartDateTimes: { [key: string]: string } = {}

    async ngOnInit() {
      try {
        this.user = JSON.parse(localStorage.getItem("user") || '{}');
        this.finishedJobs = await firstValueFrom(this.jobService.getByStatusAndUser(this.user.userId, JobStatus.Done)) as Array<Job>;
        this.finishedJobs.forEach(async job => {
          job.lastLessThanSixMonths = await firstValueFrom(this.maintenanceService.isDifferenceLessThanSixMonths(job.jobId)) as boolean;
        });
      }
      catch (error: any) {
        this.errorMsg = error.error;
      }
      try {
        this.activeMaintenance = await firstValueFrom(this.maintenanceService.getByUserIdAndJobStatusId(this.user.userId, JobStatus.Pending)) as Array<Maintenance>;
        console.log(this.activeMaintenance);
      }
      catch (error: any) { }
      try {
        const jobs = await firstValueFrom(this.maintenanceService.getByUserIdAndJobStatusId(this.user.userId, JobStatus.Accepted)) as Array<Maintenance>;
        this.activeMaintenance = this.activeMaintenance.concat(jobs);
        console.log(jobs);
      }
      catch (error: any) { }
      try {
        const jobs = await firstValueFrom(this.maintenanceService.getByUserIdAndJobStatusId(this.user.userId, JobStatus.InProgress)) as Array<Maintenance>;
        this.activeMaintenance = this.activeMaintenance.concat(jobs);
        console.log(jobs);
      }
      catch (error: any) { }
    }

    async create(job: Job){
      if (!job.maintenanceDate) {
        alert('Datum je obavezan!');
        return;
      }
      let data = {
        jobId: job.jobId,
        startDateTime: job.maintenanceDate
      }
      try {
        const m = await firstValueFrom(this.maintenanceService.create(data)) as Maintenance;
        job.lastLessThanSixMonths = true;
      }
      catch (error: any) {
        this.errorMsg = error.error;
      }
    }
}
