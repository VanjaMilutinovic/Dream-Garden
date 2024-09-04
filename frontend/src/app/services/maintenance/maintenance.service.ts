import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class MaintenanceService {

  constructor(private http: HttpClient) { }

  path :string = "http://localhost:8080/maintenance/";

  getPendingByCompanyId(companyId: number){
    let params = "?companyId="+companyId
    return this.http.get<any>(this.path+"getPendingByCompanyId"+params);
  }

  getByStatusAndCompanyId(companyId: number, status: number){
    let params = "?companyId="+companyId+"&jobStatusId="+status
    return this.http.get<any>(this.path+"getByStatusAndCompanyId"+params);
  }

  getByUserIdAndJobStatusId(userId: number, jobStatusId: number){ 
    let params = "?userId="+userId+"&jobStatusId="+jobStatusId
    return this.http.get<any>(this.path+"getByUserIdAndJobStatusId"+params);
  }

  findById(id: number){
    let params = "?maintainanceId="+id
    return this.http.get<any>(this.path+"findById"+params);
  }

  isDifferenceLessThanSixMonths(jobId: number){
    let params = "?jobId="+jobId
    return this.http.get<any>(this.path+"isDifferenceLessThanSixMonths"+params);
  }

  create(data: any){
    return this.http.post<any>(this.path+"create", data);
  }

  estimateEndDate(data: any){
    return this.http.post<any>(this.path+"estimateEndDate", data);
  }

  reject(maintainanceId: number){
    let params = "?maintainanceId="+maintainanceId
    return this.http.post<any>(this.path+"reject"+params, null);
  }
}
