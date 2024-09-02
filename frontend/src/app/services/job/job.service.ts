import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class JobsService {

  constructor(private http: HttpClient) { }

  path :string = "http://localhost:8080/job/";

  getAll(){
    return this.http.get<any>(this.path+"getAll");
  }

  getPendingByCompanyId(companyId: number){
    let params = "?companyId="+companyId;
    return this.http.get<any>(this.path+"getPendingByCompanyId"+params);
  }

  setStatus(data: any){
    return this.http.post<any>(this.path+"worker/setStatus", data);
  }

  getByStatusAndWorker(userId: number, jobStatusId: number){
    let params = "?userId="+userId+"&jobStatusId="+jobStatusId;
    return this.http.get<any>(this.path+"getByStatusAndWorker"+params);
  }

  getReviews(companyId: number){
    let params = "?companyId="+companyId
    return this.http.get<any>(this.path+"review/getByCompanyId"+params);
  }

  createJob(data: any){
    return this.http.post<any>(this.path+"create", data);
  }

  addServices(jobId: number, serviceIds: Array<number>){
    let data = {
      jobId: jobId,
      serviceIds: serviceIds
    }
    return this.http.post<any>(this.path+"service/addServices", data);
  }

  getByStatusAndUser(userId: number, jobStatusId: number){
    let params = "?userId="+userId+"&jobStatusId="+jobStatusId;
    return this.http.get<any>(this.path+"getByStatusAndUser"+params);
  }

  getreviewByJob(jobId: number){
    let params = "?jobId="+jobId;
    return this.http.get<any>(this.path+"review/getByJob"+params);
  }

  getServicesByJob(jobId: number){
    let params = "?jobId="+jobId;
    return this.http.get<any>(this.path+"service/getByJob"+params);
  }

  cancelJob(jobId: number, userId: number){
    let params = "?jobId="+jobId+"&userId="+userId;
    return this.http.post<any>(this.path+"owner/cancel"+params, null);
  }
  
}