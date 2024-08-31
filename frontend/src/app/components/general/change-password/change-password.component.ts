import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

import { sha512 } from 'js-sha512';
import { firstValueFrom } from 'rxjs';
import { User } from 'src/app/models/user.model';
import { AdminService } from 'src/app/services/admin/admin.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent {

  form: FormGroup = new FormGroup({
    username: new FormControl(""),
    oldPassword: new FormControl(""),
    newPassword: new FormControl("")
  });

  missingUsername : boolean = false;
  missingOldPassword : boolean = false;
  missingNewPassword : boolean = false;

  invalidCredentials : boolean = false;
  waitingForResponse : boolean = false;

  errorMessage : string = "";


  constructor(
    private userService: UserService,
    private adminService: AdminService,
    private router: Router
  ){

  }

  async submit(){

    if(this.waitingForResponse) return;

    const values = this.form.value;
    if(!values.username || !values.oldPassword || !values.newPassword){
      this.missingUsername = !values.username;
      this.missingOldPassword = !values.oldPassword;
      this.missingNewPassword = !values.newPassword;
      return;
    }

    const username = values.username;
    const password = sha512(values.password);

    this.invalidCredentials = false;
    this.waitingForResponse = true;
    try{
      const url = this.router.url;
      let user;
      if(url.startsWith("/admin")) user = await firstValueFrom(this.adminService.login(username,password)) as User;
      else user = await firstValueFrom(this.userService.login(username,password)) as User;

      localStorage.setItem("user",JSON.stringify(user));
      //Probao sam sa router.navigate ali kad dodjem na stranicu profila header ostane nepromenjen :(
      window.location.href = user.userTypeId.name+'/profile';
    }catch(error: any){
      this.invalidCredentials = true;
      this.errorMessage = error.error;
    }
    this.waitingForResponse = false;

  }

}
