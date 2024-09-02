import { Component } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { User } from 'src/app/models/user.model';
import { AdminService } from 'src/app/services/admin/admin.service';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { UserService } from 'src/app/services/user/user.service';
import { set } from 'date-fns';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {

  constructor(private adminService: AdminService,
    private userService: UserService,
    public sanitizer: DomSanitizer) {}

  errorMsg: any;
  currentUser: User = new User();
  cardType: string | null = null;
  cardTypeIcon: string | null = null;
  previewUrl: SafeUrl | null = null;
  imageError?: string;
  base64Image?: string | null = null;
  profilePictureInvalid?: boolean = false;
  successMessage: string | null = null;  // New property for success message
  errorMessage: string | null = null;    // New property for error message

  async ngOnInit() {
    this.currentUser = JSON.parse(localStorage.getItem('user') || '{}');
    if (this.currentUser.photoId && this.currentUser.photoId.base64) {
      this.previewUrl = this.sanitizer.bypassSecurityTrustResourceUrl(this.currentUser.photoId.base64);
    }
    this.detectCardType();
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
    const minDimension = 99;
    const maxDimension = 301;
    return (width < minDimension || height < minDimension || width > maxDimension || height > maxDimension);
  }

  async editUser(){ 
    if (this.currentUser.creditCardNumber=='' || this.currentUser.creditCardNumber==null){
      this.errorMessage = 'Broj kreditne kartice je obavezan';
      return;
    }
    if (!this.creditCardValidator()){
      this.errorMessage = 'Broj kreditne kartice nije validan';
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
      localStorage.setItem('user', JSON.stringify(this.currentUser));
      this.successMessage = 'Profil je uspešno ažuriran';
      this.errorMessage = null; // Clear any previous error message
      setTimeout(() => {
        this.successMessage = null;
      }
      , 3000);
    } catch (error) {
      this.errorMessage = 'Došlo je do greške prilikom ažuriranja korisničkih podataka.';
      this.successMessage = null; // Clear any previous success message
      setTimeout(() => {
        this.errorMessage = null;
      }
      , 3000);
    }
  }

  creditCardValidator(): boolean {
    const diners = /^(300|301|302|303|36|38)\d{12}$/;
    const mastercard = /^(51|52|53|54|55)\d{14}$/;
    const visa = /^(4539|4556|4916|4532|4929|4485|4716)\d{12}$/;
    const creditCardNumber = this.currentUser.creditCardNumber;
    return diners.test(creditCardNumber) || mastercard.test(creditCardNumber) || visa.test(creditCardNumber);
  }

  detectCardType() {
    const cardNumber = this.currentUser.creditCardNumber;
    if (/^3(00|01|02|03|6|8)\d{12}$/.test(cardNumber)) {
      this.cardType = 'Diners';
      this.cardTypeIcon = '../assets/photos/diners.png';
    } else if (/^5[1-5]\d{14}$/.test(cardNumber)) {
      this.cardType = 'MasterCard';
      this.cardTypeIcon = '../assets/photos/mastercard.png';
    } else if (/^4(539|556|916|532|929|485|716)\d{12}$/.test(cardNumber)) {
      this.cardType = 'Visa';
      this.cardTypeIcon = '../assets/photos/visa.png';
    } else {
      this.cardType = null;
      this.cardTypeIcon = null;
    }
  }
}
