import { Component } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { User } from 'src/app/models/user.model';
import { AdminService } from 'src/app/services/admin/admin.service';
import { DomSanitizer } from '@angular/platform-browser';
import { UserService } from 'src/app/services/user/user.service';


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {

  constructor(private adminService: AdminService,
    private userService: UserService,
    public sanitizer: DomSanitizer){}

  errorMsg: any;
  currentUser: User = new User();
  cardType: string | null = null;
  cardTypeIcon: string | null = null;

  async ngOnInit() {
    this.currentUser = JSON.parse(localStorage.getItem('user') || '{}');
  }

  async editUser(){ 
    let data = {
      userId: this.currentUser.userId,
      email: this.currentUser.email,
      name: this.currentUser.name,
      lastname: this.currentUser.lastname,
      address: this.currentUser.address,
      contactNumber: this.currentUser.contactNumber,
      creditCardNumber: this.currentUser.creditCardNumber,
      base64: this.currentUser.photoId.base64
    }
    this.currentUser = await firstValueFrom(this.userService.update(data)) as User;
  }
}
