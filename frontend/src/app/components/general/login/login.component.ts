import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  form: FormGroup = new FormGroup({
    username: new FormControl(""),
    password: new FormControl("")
  });

  missingCredential : boolean = false;

  submit(){
    const values = this.form.value;
    if(!values.username || !values.password){
      this.missingCredential = true;
      return;
    }
  }

}
