import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ServicesService {

  constructor(private http: HttpClient) { }

  path :string = "http://localhost:8080/service/";

  
  createService(service: any){
    return this.http.post<any>(this.path+"create", service);
  }
}
