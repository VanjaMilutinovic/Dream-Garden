import { Component } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Maintenance } from 'src/app/models/maintenance.model';
import { User } from 'src/app/models/user.model';
import { Worker } from 'src/app/models/worker.model';
import { JobsService } from 'src/app/services/job/job.service';
import { MaintenanceService } from 'src/app/services/maintenance/maintenance.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-maintenance-decorator',
  templateUrl: './maintenance.component.html',
  styleUrls: ['./maintenance.component.css']
})
export class MaintenanceComponent {

  constructor(private jobService: JobsService,
    private maintenanceService: MaintenanceService,
    private userService: UserService){}

    pendingJobs: Array<Maintenance> = [];
    user: User = new User();
    worker: Worker = new Worker();
    errorMsg: string = "";

    async ngOnInit() {
      try {
        this.user = JSON.parse(localStorage.getItem("user") || '{}');
        this.worker = await firstValueFrom(this.userService.getWorker(this.user.userId)) as Worker;
        this.pendingJobs = await firstValueFrom(this.maintenanceService.getPendingByCompanyId(this.worker.companyId.companyId)) as Array<Maintenance>;
      }
      catch (error: any) { }
    }

    async accept(m: Maintenance){
      if (m.estimatedEndDateTime === null) {
        alert("Unesite vreme završetka održavanja");
        return;
      }
      try {
        let data = {
          maintainanceId: m.maintenanceId,
          workerUserId: this.user.userId,
          estimatedEndDateTime: m.estimatedEndDateTime
        }
        await firstValueFrom(this.maintenanceService.estimateEndDate(data));
        this.pendingJobs = this.pendingJobs.filter(maintenance => maintenance.maintenanceId !== m.maintenanceId);
      }
      catch (error: any) {
        this.errorMsg = error.error;
      }
    }

    async reject(m: Maintenance){
      try {
        await firstValueFrom(this.maintenanceService.reject(m.maintenanceId));
        this.pendingJobs = this.pendingJobs.filter(maintenance => maintenance.maintenanceId !== m.maintenanceId);
      }
      catch (error: any) {
        this.errorMsg = error.error;
      }
    }

}
