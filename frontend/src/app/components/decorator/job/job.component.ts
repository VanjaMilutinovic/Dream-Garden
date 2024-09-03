import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { JobsService } from 'src/app/services/job/job.service';
import { User } from 'src/app/models/user.model';
import { UserService } from 'src/app/services/user/user.service';
import { Job } from 'src/app/models/job.model';
import { Worker } from 'src/app/models/worker.model';
import { JobStatus } from 'src/app/enums/job-status.enum';

@Component({
  selector: 'app-job',
  templateUrl: './job.component.html',
  styleUrls: ['./job.component.css']
})
export class JobComponent {

  constructor(private jobService: JobsService,
              private userService: UserService,
              private router: Router) {}

  user: User = new User();
  worker: Worker = new Worker();
  pendingJobs: Array<Job> = [];
  acceptedJobs: Array<Job> = [];
  finishedJobs: Array<Job> = [];
  errorMsg: string = "";

  jobImageMap: { [key: number]: { files: File[], count: number } } = {};  // Map jobId to files and count

  async ngOnInit() {
    try {
      this.user = JSON.parse(localStorage.getItem("user") || '{}');
      this.worker = await firstValueFrom(this.userService.getWorker(this.user.userId)) as Worker;
      const jobs = await firstValueFrom(this.jobService.getPendingByCompanyId(this.worker.companyId.companyId)) as Array<Job>;
      jobs.sort((b, a) => new Date(a.requestDateTime).getTime() - new Date(b.requestDateTime).getTime());
      this.pendingJobs = jobs;
    } catch (error: any) {
      this.errorMsg = error.error;
    }

    try {
      const inProgressJobs = await firstValueFrom(this.jobService.getByStatusAndWorker(this.user.userId, JobStatus.InProgress)) as Array<Job>;
      this.acceptedJobs = inProgressJobs;
    } catch (error: any) {
      this.errorMsg = error.error;
    }

    try {
      const acceptedJobs = await firstValueFrom(this.jobService.getByStatusAndWorker(this.user.userId, JobStatus.Accepted)) as Array<Job>;
      acceptedJobs.forEach(job => {
        this.acceptedJobs.push(job);
      });
    } catch (error: any) {
      this.errorMsg = error.error;
    }

    try {
      const finishedJobs = await firstValueFrom(this.jobService.getByStatusAndWorker(this.user.userId, JobStatus.Done)) as Array<Job>;
      finishedJobs.forEach(job => {
        this.finishedJobs.push(job);
      });
    } catch (error: any) {
      this.errorMsg = error.error;
    }
  }

  onFileChange(event: Event, jobId: number) {
    const input = event.target as HTMLInputElement;
    const files = input.files ? Array.from(input.files) : [];

    if (!this.jobImageMap[jobId]) {
      this.jobImageMap[jobId] = { files: [], count: 0 };
    }
    this.jobImageMap[jobId].files = files;
    this.jobImageMap[jobId].count = files.length;
  }

  async uploadImages(jobId: number) {
    const jobData = this.jobImageMap[jobId];

    if (jobData && jobData.files.length > 0) {
      try {
        const uploadPromises = jobData.files.map(file => this.readFileAsBase64(file));
        const base64Images = await Promise.all(uploadPromises);

        for (const base64Image of base64Images) {
          const data = {
            jobId: jobId,
            base64: base64Image
          };
          await firstValueFrom(this.jobService.uploadImage(data));
        }

        alert('Images uploaded successfully for job ID: ' + jobId);
        this.jobImageMap[jobId].files = [];
        this.jobImageMap[jobId].count = 0;

        const fileInput: HTMLInputElement | null = document.querySelector(`input[type="file"][data-job-id="${jobId}"]`);
        if (fileInput) {
          fileInput.value = '';  // Reset the file input for the next upload
        }
      } catch (error) {
        console.error('Error uploading images:', error);
        this.errorMsg = 'Error occurred while uploading images for job ID: ' + jobId;
      }
    } else {
      alert('No images selected for job ID: ' + jobId);
    }
  }

  private readFileAsBase64(file: File): Promise<string> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => {
        const base64Image = reader.result as string;
        resolve(base64Image);
      };
      reader.onerror = error => reject(error);
      reader.readAsDataURL(file);
    });
  }

  async accept(job: Job) {
    let data = {
      jobId: job.jobId,
      statusId: JobStatus.Accepted,
      userWorkerId: this.user.userId
    };
    await firstValueFrom(this.jobService.setStatus(data));
    const index = this.pendingJobs.indexOf(job);
    if (index > -1) {
      this.pendingJobs.splice(index, 1);
    }
    this.acceptedJobs.push(job);
  }

  async reject(job: Job) {
    if (job.rejectedDescription == "" || job.rejectedDescription == null) {
      alert("Obavezan je razlog odbijanja posla!");
      return;
    }
    let data = {
      jobId: job.jobId,
      statusId: JobStatus.Rejected,
      rejectionDescription: job.rejectedDescription,
      userWorkerId: this.user.userId
    };
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
    };
    await firstValueFrom(this.jobService.setStatus(data));
    const index = this.acceptedJobs.indexOf(job);
    if (index > -1) {
      this.acceptedJobs.splice(index, 1);
    }
    this.finishedJobs.push(job);
  }
}
