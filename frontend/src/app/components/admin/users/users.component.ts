import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { User } from 'src/app/models/user.model';
import { AdminService } from 'src/app/services/admin/admin.service';
import { UserStatus } from 'src/app/enums/user-status.enum';
import { CompaniesService } from 'src/app/services/company/company.service';


@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent {

  constructor(private adminService: AdminService,
              private router: Router){}
  
  allUsers :Array<User> = [];
  viewUsers :Array<User> = [];
  errorMsg: any;
  currentUser: User = new User();
  currenyUserFlag: boolean = false;

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
  editUser(){ }

  createNewDecorator(){
    this.router.navigate(['/admin/users/createWorker']);
  }
}
