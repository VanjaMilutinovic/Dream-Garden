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
}
