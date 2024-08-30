import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PhotoService {

  constructor(private http: HttpClient) { }

  path :string = "http://localhost:8080/";

  getTopK(k:number){
    let params = "?k="+k;
    return this.http.get<any>(this.path+"job/photo/getTopK" + params);
  }
}
