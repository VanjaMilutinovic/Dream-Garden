import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { sha512 } from 'js-sha512';
import { firstValueFrom } from 'rxjs';
import { UserService } from 'src/app/services/user/user.service';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  constructor(private userService: UserService){}

  form: FormGroup = new FormGroup({
    username: new FormControl(""),
    password: new FormControl("")
  });

  missingUsername : boolean = false;
  missingPassword : boolean = false;

  invalidCredentials : boolean = false;
  waitingForResponse : boolean = false;


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
      const user = await firstValueFrom(this.userService.login(username,password));
    }catch(error: any){
      switch(error.status){
        case 401:
          //Invalid credentials
          this.invalidCredentials = true;
          break;
      }
    }
    // console.log(user);
    this.waitingForResponse = false;

  }

}
