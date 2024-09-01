import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CompaniesService {

  constructor(private http: HttpClient) { }

  path :string = "http://localhost:8080/company/";

  getAll(){
    return this.http.get<any>(this.path+"getAll");
  }

  getAllCompaniesWithWorkers(){
    return this.http.get<any>(this.path+"getAllCompaniesWithWorkers");
  }

  create(company:any){
    return this.http.post<any>(this.path+"create", company);
  }

  createHoliday(companyId:number, start:Date, end:Date){
    let data = {
      "companyId": companyId, 
      "startDateTime": start, 
      "endDateTime": end
    };
    return this.http.post<any>(this.path+"holyday/create", data);
  }

  getHolidays(companyId:number){
    let params = "?companyId="+companyId;
    return this.http.get<any>(this.path+"holyday/getByCompanyId"+params);
  }

  update(data:any){
    return this.http.post<any>(this.path+"update", data);
  }
}
