import { Component, NgZone } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { sha512 } from 'js-sha512';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user/user.service';
import { firstValueFrom } from 'rxjs/internal/firstValueFrom';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { HttpClient } from '@angular/common/http';

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
  isCheckingImage: boolean = false;
  previewUrl: SafeUrl | null = null;
  imageError?: string;
  base64Image?: string | null =null;
  profilePictureInvalid?: boolean=false;

  constructor(
    private fb: FormBuilder, 
    private userService: UserService, 
    private router: Router, 
    private sanitizer: DomSanitizer
  ) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      hashedPassword: ['', [Validators.required, Validators.pattern(/^(?=[A-Za-z])(?=.*[A-Z])(?=(.*[a-z]){3,})(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{6,10}$/)]],
      name: ['', Validators.required],
      lastname: ['', Validators.required],
      gender: ['', Validators.required],
      address: ['', Validators.required],
      contactNumber: ['', [Validators.required, Validators.pattern(/^\+?\d{9,15}$/)]],
      email: ['', [Validators.required, Validators.email]],
      creditCardNumber: ['', [Validators.required]],
      photo: [null]
    });
  }

  get f() { return this.registerForm.controls; }

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
          
          console.log('Starting Base64 conversion'); // Debugging log
          const reader = new FileReader();
          reader.onload = () => {
            this.base64Image = (reader.result as string); // Remove the base64 prefix
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
    // Define the minimum and maximum dimensions
    const minDimension = 99;
    const maxDimension = 301;
  
    // Check if the dimensions are outside the allowed range
    return (width < minDimension || height < minDimension || width > maxDimension || height > maxDimension);
  }

  async onSubmit() {
    if (this.waitingForResponse) return;
    
    if (this.registerForm.invalid || this.profilePictureInvalid) {
      this.errorMessage = "Molimo Vas da popunite sva obavezna polja.";
        return;
    }

    const formData = {
      username: this.registerForm.get('username')?.value,
      hashedPassword: sha512(this.registerForm.get('hashedPassword')?.value),
      name: this.registerForm.get('name')?.value,
      lastname: this.registerForm.get('lastname')?.value,
      gender: this.registerForm.get('gender')?.value,
      address: this.registerForm.get('address')?.value,
      contactNumber: this.registerForm.get('contactNumber')?.value,
      email: this.registerForm.get('email')?.value,
      creditCardNumber: this.registerForm.get('creditCardNumber')?.value,
      userTypeId: '1', // Assuming '1' for owner
      base64: this.base64Image // The path where the photo is temporarily saved
    };
    this.errorMessage = "";
    this.successMessage = "";
    this.waitingForResponse = true;

    try {
      const user: any = await firstValueFrom(this.userService.register(formData));
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
  

  // Handle CAPTCHA resolved event
  onCaptchaResolved(captchaResponse: string): void {
    if (captchaResponse) {
      this.registerForm.controls['recaptcha'].setValue(captchaResponse);
      this.captchaError = false;
    } else {
      this.captchaError = true;
    }
  }

  onCaptchaError(error: any): void {
    this.captchaError = true;
  }
}
