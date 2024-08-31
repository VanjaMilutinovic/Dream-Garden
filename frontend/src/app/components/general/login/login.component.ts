import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { sha512 } from 'js-sha512';
import { firstValueFrom } from 'rxjs';
import { User } from 'src/app/models/user.model';
import { AdminService } from 'src/app/services/admin/admin.service';
import { UserService } from 'src/app/services/user/user.service';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  constructor(
    private userService: UserService,
    private adminService: AdminService,
    private router: Router
  ){
    const user = localStorage.getItem("user");
    if(user){
      try{
        const type = (JSON.parse(user) as User).userTypeId.name;
        this.router.navigate([type+'/profile'])
      }catch(error){
        console.warn("Nekako se u localStorage upisao nevalidan User");
      }
    }
  }

  form: FormGroup = new FormGroup({
    username: new FormControl(""),
    password: new FormControl("")
  });

  missingUsername : boolean = false;
  missingPassword : boolean = false;

  invalidCredentials : boolean = false;
  waitingForResponse : boolean = false;

  errorMessage : string = "";


  async submit(){

    if(this.waitingForResponse) return;

    const values = this.form.value;
    if(!values.username || !values.password){
      this.missingUsername = !values.username;
      this.missingPassword = !values.password;
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
