import { Component } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { JobStatus } from 'src/app/enums/job-status.enum';
import { Job } from 'src/app/models/job.model';
import { Maintenance } from 'src/app/models/maintenance.model';
import { User } from 'src/app/models/user.model';

import { JobsService } from 'src/app/services/job/job.service';
import { MaintenanceService } from 'src/app/services/maintenance/maintenance.service';

@Component({
  selector: 'app-maintenance',
  templateUrl: './maintenance.component.html',
  styleUrls: ['./maintenance.component.css']
})
export class MaintenanceComponent {
  constructor(private jobService: JobsService,
    private maintenanceService: MaintenanceService){}

    finishedJobs: Array<Job> = [];
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
