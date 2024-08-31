import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

import { sha512 } from 'js-sha512';
import { firstValueFrom } from 'rxjs';
import { User } from 'src/app/models/user.model';
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

  waitingForResponse : boolean = false;
  errorMessage : string = "";


  constructor(private userService: UserService){}

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

    const oldPassword = values.oldPassword;
    const newPassword = values.newPassword;
    if(
      !newPassword.toString().match(/^(?=.*[A-Z])(?=(.*[a-z]){3,})(?=.*\d)[A-Za-z][A-Za-z\d\W_]{5,9}$/g) 
    ){
      this.errorMessage = "New password fails the regex";
      return;
    }

    this.errorMessage = "";
    this.waitingForResponse = true;

    try{
      const user = await firstValueFrom(this.userService.changePassword(username,sha512(oldPassword), sha512(newPassword))) as User;
      localStorage.setItem("user",JSON.stringify(user));
      //Probao sam sa router.navigate ali kad dodjem na stranicu profila header ostane nepromenjen :(
      window.location.href = user.userTypeId.name+'/profile';
    }catch(error: any){
      this.errorMessage = error.error;
    }
    this.waitingForResponse = false;

  }

}
