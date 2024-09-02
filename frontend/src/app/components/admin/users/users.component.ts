import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { User } from 'src/app/models/user.model';
import { AdminService } from 'src/app/services/admin/admin.service';
import { UserStatus } from 'src/app/enums/user-status.enum';
import { DomSanitizer } from '@angular/platform-browser';
import { UserService } from 'src/app/services/user/user.service';


@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent {

  constructor(private adminService: AdminService,
              private userService: UserService,
              private router: Router,
              public sanitizer: DomSanitizer){}
  
  allUsers :Array<User> = [];
  viewUsers :Array<User> = [];
  errorMsg: any;
  currentUser: User = new User();
  currenyUserFlag: boolean = false;
  selectedUserType: number = 0;
  selectedUserStatus: number = 0;

  async ngOnInit() {
    try {
      const users = await firstValueFrom(this.adminService.getAll()) as Array<User>;
      this.allUsers = users;
      this.viewUsers = users;
    }
    catch(error){
      this.errorMsg = error;
      console.log(error);
    }
  }

  filterUsers() {
    this.viewUsers = [];
    this.allUsers.forEach(u => {
      if ((this.selectedUserType == 0 || u.userTypeId.userTypeId == this.selectedUserType) &&
      (this.selectedUserStatus == 0 || u.userStatusId.userStatusId == this.selectedUserStatus))
        this.viewUsers.push(u);
    });
  }

  async activate(user: User){
    try {
      const u = await firstValueFrom(this.adminService.setUserStatus(user.userId, UserStatus.Active)) as User;
      window.location.reload();
    }
    catch(error){
      this.errorMsg = error;
      console.log(error);
    }
  }
  async reject(user: User){
    try {
      const u = await firstValueFrom(this.adminService.setUserStatus(user.userId, UserStatus.Denied)) as User;
      window.location.reload();
    }
    catch(error){
      this.errorMsg = error;
      console.log(error);
    }
  }
  async unblock(user: User){
    try {
      const u = await firstValueFrom(this.adminService.setUserStatus(user.userId, UserStatus.Active)) as User;
      window.location.reload();
    }
    catch(error){
      this.errorMsg = error;
      console.log(error);
    }
  }
  async block(user: User){
    try {
      const u = await firstValueFrom(this.adminService.setUserStatus(user.userId, UserStatus.Blocked)) as User;
      window.location.reload();
    }
    catch(error){
      this.errorMsg = error;
      console.log(error);
    }
  }

  show(user: User){
    this.currentUser = user;
    this.currenyUserFlag = true;
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
    this.allUsers = await firstValueFrom(this.adminService.getAll()) as Array<User>;
  }

  createNewDecorator(){
    this.router.navigate(['/admin/users/createWorker']);
  }
}
