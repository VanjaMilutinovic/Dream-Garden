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
}