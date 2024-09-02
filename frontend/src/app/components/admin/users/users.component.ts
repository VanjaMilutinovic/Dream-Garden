import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { User } from 'src/app/models/user.model';
import { AdminService } from 'src/app/services/admin/admin.service';
import { UserStatus } from 'src/app/enums/user-status.enum';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
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
  previewUrl: SafeUrl | null = null;
  imageError?: string|null;
  base64Image?: string | null = null;
  profilePictureInvalid?: boolean = false;
  successMessage: string | null = null;  // Success message for updates
  errorMessage: string | null = null;    // Error message for updates

  async ngOnInit() {
    try {
      this.allUsers=[];
      this.viewUsers=[];
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
    if (this.currentUser.photoId && this.currentUser.photoId.base64) {
      this.previewUrl = this.sanitizer.bypassSecurityTrustResourceUrl(this.currentUser.photoId.base64);
    } else {
      this.previewUrl = null;
    }
  }

  onFileChange(event: any) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      const img = new Image();

      img.src = URL.createObjectURL(file);

      img.onload = () => {
        const width = img.width;
        const height = img.height;

        if (this.isImageDimensionInvalid(width, height)) {
          this.imageError = 'Slika mora biti dimenzija 100x100 do 300x300 piksela.';
          this.profilePictureInvalid = true;
        } else {
          this.imageError = '';
          this.profilePictureInvalid = false;

          // Set the preview URL
          this.previewUrl = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(file));

          const reader = new FileReader();
          reader.onload = () => {
            this.base64Image = (reader.result as string);
          };
          reader.readAsDataURL(file);
        }
      };
      img.onerror = () => {
        this.imageError = 'Nepravilan format slike.';
        this.profilePictureInvalid = true;
        this.previewUrl = null;
        this.base64Image = null;
      };
    } else {
      this.profilePictureInvalid = false;
      this.previewUrl = null;
      this.base64Image = null;
    }
  }

  isImageDimensionInvalid(width: number, height: number): boolean {
    const minDimension = 100;
    const maxDimension = 300;
    return (width < minDimension || height < minDimension || width > maxDimension || height > maxDimension);
  }

  async editUser(){ 
    if (this.profilePictureInvalid) {
      this.errorMessage = 'Molimo vas da unesete ispravnu sliku.';
      return;
    }
  
    let data = {
      userId: this.currentUser.userId,
      email: this.currentUser.email,
      name: this.currentUser.name,
      lastname: this.currentUser.lastname,
      address: this.currentUser.address,
      contactNumber: this.currentUser.contactNumber,
      creditCardNumber: this.currentUser.creditCardNumber,
      base64: this.base64Image || this.currentUser.photoId.base64
    }
  
    try {
      this.currentUser = await firstValueFrom(this.userService.update(data)) as User;
      this.successMessage = 'Profil korisnika je uspešno ažuriran.';
      this.errorMessage = null;
      
      // Clear the file input
      const fileInput: any = document.querySelector('input[type="file"]');
      if (fileInput) {
        fileInput.value = '';
      }
  
      this.base64Image = null;
      this.imageError = null;
  
      // Refresh the profile data
      setTimeout(() => {
        this.successMessage = null;
      }, 3000);
      
      this.ngOnInit();
    } catch (error) {
      this.errorMessage = 'Došlo je do greške prilikom ažuriranja korisničkog profila.';
      this.successMessage = null;
  
      setTimeout(() => {
        this.errorMessage = null;
      }, 3000);
    }
  }
  
  createNewDecorator(){
    this.router.navigate(['/admin/users/createWorker']);
  }
}
