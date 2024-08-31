import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient) { }

  path :string = "http://localhost:8080/admin/";

  getAll(){
    return this.http.get<any>(this.path+"user/getAll");
  }

  setUserStatus(userId: number, userStatusId: number){
    let params = "?userId="+userId+"&userStatusId="+userStatusId;
    return this.http.post<any>(this.path+"user/setStatus"+params, null);
  }

  login(username:string, hashedPassword:string){
    let params = "?username="+username+"&hashedPassword="+hashedPassword;
    return this.http.get<any>(this.path+"login" + params);
  }
}
