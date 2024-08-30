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
}
