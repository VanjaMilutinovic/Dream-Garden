import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class StatisticService {

  constructor(private http: HttpClient) { }

  path :string = "http://localhost:8080/statistic/";

  getByRequestTime(){
    return this.http.get<any>(this.path+"job/getByRequestTime");
  }

  getByWorker(workerId: number){
    let params = "?workerId="+workerId;
    return this.http.get<any>(this.path+"job/getByWorker"+params);
  }

  getByCompany(companyId: number){
    let params = "?companyId="+companyId;
    return this.http.get<any>(this.path+"job/getByCompany"+params);
  }
  getByDay(){
    return this.http.get<any>(this.path+"job/getByDay");
  }
}
