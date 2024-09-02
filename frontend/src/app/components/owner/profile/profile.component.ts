import { Component } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { User } from 'src/app/models/user.model';
import { AdminService } from 'src/app/services/admin/admin.service';
import { DomSanitizer } from '@angular/platform-browser';
import { UserService } from 'src/app/services/user/user.service';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';


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
    this.detectCardType();
  }

  async editUser(){ 
    if (this.currentUser.creditCardNumber=='' || this.currentUser.creditCardNumber==null){
      alert('Broj kreditne kartice je obavezan');
      return;
    }
    if (this.creditCardValidator()==false){
      alert('Broj kreditne kartice nije validan');
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
      base64: this.currentUser.photoId.base64
    }
    this.currentUser = await firstValueFrom(this.userService.update(data)) as User;
    localStorage.removeItem('user');
    localStorage.setItem('user', JSON.stringify(this.currentUser));
  }

  creditCardValidator(): boolean {
    const diners = /^(300|301|302|303|36|38)\d{12}$/;
    const mastercard = /^(51|52|53|54|55)\d{14}$/;
    const visa = /^(4539|4556|4916|4532|4929|4485|4716)\d{12}$/;
    const creditCardNumber = this.currentUser.creditCardNumber;
    // Check if the credit card number matches any of the regexes
    if (diners.test(creditCardNumber) || mastercard.test(creditCardNumber) || visa.test(creditCardNumber)) {
        return true;
      }
    return false;
    
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
