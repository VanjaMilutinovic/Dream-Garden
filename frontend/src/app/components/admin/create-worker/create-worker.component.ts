import { Component } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { User } from 'src/app/models/user.model';
import { AdminService } from 'src/app/services/admin/admin.service';
import { DomSanitizer } from '@angular/platform-browser';
import { UserService } from 'src/app/services/user/user.service';
import { sha512 } from 'js-sha512';
import { Router } from '@angular/router';


@Component({
  selector: 'app-create-worker',
  templateUrl: './create-worker.component.html',
  styleUrls: ['./create-worker.component.css']
})
export class CreateWorkerComponent {

  constructor(private adminService: AdminService,
    private userService: UserService,
    public sanitizer: DomSanitizer,
    private router: Router){}

  errorMsg: any;
  currentUser: User = new User();
  cardType: string | null = null;
  cardTypeIcon: string | null = null;

  async editUser(){ 
    if (this.currentUser.username.length == 0
      || this.currentUser.hashedPassword.length == 0
      || this.currentUser.email.length == 0
      || this.currentUser.name.length == 0
      || this.currentUser.lastname.length == 0
      || this.currentUser.address.length == 0
      || this.currentUser.contactNumber.length == 0){
      alert("Sva polja su obavezna!");
      return;
    }
    if (!this.currentUser.hashedPassword.toString().match(/^(?=.*[A-Z])(?=(.*[a-z]){3,})(?=.*\d)[A-Za-z][A-Za-z\d\W_]{5,9}$/g)) {
      alert("Llozinka u neodgovarajućem formatu!");
      return;
    }
    let data = {
      username: this.currentUser.username,
      hashedPassword: sha512(this.currentUser.hashedPassword),
      email: this.currentUser.email,
      name: this.currentUser.name,
      lastname: this.currentUser.lastname,
      address: this.currentUser.address,
      contactNumber: this.currentUser.contactNumber,
      gender: this.currentUser.gender,
      creditCardNumber: '',
      base64: null
    }
    try {
      this.currentUser = await firstValueFrom(this.adminService.createWorker(data)) as User;
      this.errorMsg = "";
      this.router.navigate(['/admin/users']);
    }
    catch (error: any) {
      this.errorMsg = error.error;
    }
  }
}