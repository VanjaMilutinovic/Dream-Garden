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
}