import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { sha512 } from 'js-sha512';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user/user.service';
import { firstValueFrom } from 'rxjs/internal/firstValueFrom';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup;
  cardType: string | null = null;
  cardTypeIcon: string | null = null;
  captchaToken: string | null = null;
  captchaError: boolean = false;
  errorMessage: string | null = null;
  successMessage: string | null = null;
  waitingForResponse: boolean = false;

  constructor(private fb: FormBuilder, private userService: UserService, private router: Router) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      hashedPassword: ['', [Validators.required, Validators.pattern(/^(?=[A-Za-z])(?=.*[A-Z])(?=(.*[a-z]){3,})(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{6,10}$/)]],
      name: ['', Validators.required],
      lastname: ['', Validators.required],
      gender: ['', Validators.required],
      address: ['', Validators.required],
      contactNumber: ['', [Validators.required, Validators.pattern(/^\+?\d{9,15}$/)]],
      email: ['', [Validators.required, Validators.email]],
      creditCardNumber: ['', [Validators.required, this.creditCardValidator()]],
      photo: [null, Validators.required]
    });
  }

  get f() { return this.registerForm.controls; }

  onFileChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      const img = new Image();
      img.onload = () => {
        if (img.width >= 100 && img.width <= 300 && img.height >= 100 && img.height <= 300) {
          this.registerForm.patchValue({ photo: file });
        } else {
          this.registerForm.get('photo')?.setErrors({ invalidSize: true });
        }
      };
      img.onerror = () => {
        this.registerForm.get('photo')?.setErrors({ invalidType: true });
      };
      img.src = URL.createObjectURL(file);
    }
  }

  detectCardType() {
    const cardNumber = this.registerForm.get('creditCardNumber')?.value;
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
  

  handleCaptchaSuccess(captchaResponse: string): void {
    this.captchaToken = captchaResponse;
    this.captchaError = false;
  }

  async onSubmit() {
    if (this.waitingForResponse) return;

    this.f['photo'].markAsTouched(); // Ensures photo validation triggers

    if (!this.captchaToken) {
      this.captchaError = true;
      return;
    }

    if (this.registerForm.invalid) {
      return;
    }

    const formData = new FormData();
    Object.keys(this.registerForm.controls).forEach(key => {
      if (key === 'hashedPassword') {
        formData.append(key, sha512(this.registerForm.get(key)?.value));
      } else {
        formData.append(key, this.registerForm.get(key)!.value);
      }
    });

    this.errorMessage = "";
    this.successMessage = "";
    this.waitingForResponse = true;

    try {
      const response = await firstValueFrom(this.userService.register(formData));
      this.successMessage = "Korisnik je uspešno registrovan.";
      this.router.navigate(['/login']);
    } catch (error: any) {
      if (error.status === 406) {
        this.errorMessage = error.error || "Zahtev za registraciju nije adekvatan.";
      } else if (error.status === 409) {
        this.errorMessage = "Korisničko ime ili email već postoje.";
      } else if (error.status === 404) {
        this.errorMessage = "Tip korisnika ili status nisu pronađeni.";
      } else {
        this.errorMessage = "Došlo je do greške prilikom registracije.";
      }
    } finally {
      this.waitingForResponse = false;
    }
  }

  creditCardValidator() {
    return (control: AbstractControl) => {
      const value = control.value.replace(/\s+/g, '');
      const diners = /^(300|301|302|303|36|38)\d{12}$/;
      const mastercard = /^(51|52|53|54|55)\d{14}$/;
      const visa = /^(4539|4556|4916|4532|4929|4485|4716)\d{12}$/;

      if (diners.test(value) || mastercard.test(value) || visa.test(value)) {
        return null;
      }
      return { invalidCard: true };
    };
  }
}
