import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  path :string = "http://localhost:8080/user/";

  getByUserTypeId(userTypeId:number){
    let params = "?userTypeId="+userTypeId;
    return this.http.get<any>(this.path+"getByUserTypeId" + params);
  }

  login(username:string, hashedPassword:string){
    let params = "?username="+username+"&hashedPassword="+hashedPassword;
    return this.http.get<any>(this.path+"login" + params);
  }

  changePassword(username:string, oldHashedPassword:string, newHashedPassword:string){
    let params = "?username="+username+"&oldHashedPassword="+oldHashedPassword+"&newHashedPassword="+newHashedPassword;
    return this.http.post<any>(this.path+"changePassword" + params, null);
  }

  register(formData: any)  {
    return this.http.post<any>(this.path + "register", formData);
  }


}