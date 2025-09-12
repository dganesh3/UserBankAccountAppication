import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl, AbstractControlOptions } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { json } from 'stream/consumers';
import { setLocalStorageItem } from '../../core/services/storage.util';
import { HttpClient } from '@angular/common/http';
import Swal from 'sweetalert2';
import { AuthService } from '../../authservice';


@Component({
  selector: 'app-signup',
  templateUrl: './signup.html',
  styleUrls: ['./signup.css'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterModule]
})
export class SignupComponent implements OnInit {

   otpSent = false;
  otpError = false;
  emailVerified = false;
  countdown = 0;
  private timer: any;

  signupForm!: FormGroup;
  submitted = false;
  

  constructor(private fb: FormBuilder, private router: Router, private authService: AuthService) { }

  ngOnInit(): void {
  this.signupForm = this.fb.group({
    firstname: ['', Validators.required],
    lastname: ['', Validators.required],
    username: ['', Validators.required],
    mobile: ['', [Validators.required, Validators.pattern('^[0-9]{10}$'),Validators.minLength(10), Validators.maxLength(10)]],
    email: ['', [Validators.required, Validators.email]],
    otp: ['', Validators.required],
    password: ['', [Validators.required, Validators.minLength(6)]],
    confirmpassword: ['', Validators.required],
  }, {
    validators: passwordMatchValidator
  } as AbstractControlOptions);
    // this.signupForm = new FormGroup({
    //   firstname: new FormControl('', Validators.required),
    //   lastname: new FormControl('', Validators.required),
    //   username: new FormControl('', Validators.required),
    //   mobile: new FormControl('', [
    //     Validators.required,
    //     Validators.pattern('^[0-9]{10}$')
    //   ]),
    //   email: new FormControl('', [
    //     Validators.required,
    //     Validators.email
    //   ]),
    //   password: new FormControl('', [
    //     Validators.required,
    //     Validators.minLength(6)
    //   ]),
    //   confirmpassword: new FormControl('', Validators.required)
    // }, {
    //   validators: passwordMatchValidator
    // } as AbstractControlOptions);
  }


  allowOnlyNumbers(event: KeyboardEvent) {
  const charCode = event.which ? event.which : event.keyCode;
  if (charCode < 48 || charCode > 57) {
    event.preventDefault(); // blocks non-numeric input
  }
}

    register(): void {
    if (!this.emailVerified) {
      console.warn('Email not verified. Cannot register.');
      return;
    }

    const signupData = {
      firstname: this.signupForm.get('firstname')?.value,
    lastname: this.signupForm.get('lastname')?.value,
    username: this.signupForm.get('username')?.value,
    mobile: this.signupForm.get('mobile')?.value,
    email: this.signupForm.get('email')?.value,
    password: this.signupForm.get('password')?.value,
    confirmpassword: this.signupForm.get('confirmpassword')?.value
    };

    const otp = this.signupForm.get('otp')?.value;

    this.authService.register(signupData, otp).subscribe({
      next: res => {
        console.log('User registered successfully:', res);
        Swal.fire({
          icon: 'success',
          title: 'Registration successful!',
          text: '✅ Registration successful!',
          timer: 1500,
          showConfirmButton: false
        }).then(() => {
          this.router.navigate(['/login']);
        });
      },
      error: err => {
        Swal.fire({
          icon: 'error',
          title: 'Registration failed!',
          text: err.error?.message || 'Something went wrong. Please try again.'
        }).then(() => {
          window.location.reload();
        });
      }
    });
  }


// onSubmit(): void {
//   this.submitted = true;

//   // 🔒 Block submit if form invalid
//   if (this.signupForm.invalid) return;

//   // 🔒 Block submit if email not verified
//   if (!this.emailVerified) {
//     Swal.fire({
//       icon: 'warning',
//       title: 'Email Verification Required',
//       text: 'Please verify your email before signing up.',
//       timer: 2000,
//       showConfirmButton: false
//     });
//     return;
//   }

//   this.authService.register(this.signupForm.value).subscribe({
//     next: (res) => {
//       Swal.fire({
//         icon: 'success',
//         title: 'Signup Successful',
//         text: 'You can now login with your credentials',
//         timer: 1500,
//         showConfirmButton: false
//       }).then(() => {
//         this.router.navigate(['/login']);
//       });
//     },
//     error: (err) => {
//       Swal.fire({
//         icon: 'error',
//         title: 'Signup Failed',
//         text: err.error?.message || 'Something went wrong. Please try again.'
//       });
//     }
//   });
// }



  sendOtp(): void {
  const email = this.signupForm.get('email')?.value;

  if (this.signupForm.get('email')?.invalid) {
    alert('Invalid email format');
    return;
  }

  // Step 1: Check if email exists
  this.authService.checkEmailExists(email).subscribe({
    next: exists => {
      if (exists) {
        alert('Email already registered! Try with a different email.');
        return; // stop OTP sending
      }

      // Step 2: Send OTP if email does not exist
      this.authService.sendOtp(email).subscribe({
        next: res => {
          alert('OTP Sent Successfully');
          console.log('OTP Sent:', res);
          this.otpSent = true;
        },
        error: err => alert('Error sending OTP: ' + err)
      });
    },
    error: err => alert('Error checking email existence: ' + err)
  });
}

  resendOtp() {
    this.startCountdown(30);
    alert('OTP Resent Successfully');
    this.sendOtp(); // Same flow
  }

  private startCountdown(seconds: number) {
    this.countdown = seconds;
    clearInterval(this.timer);
    this.timer = setInterval(() => {
      if (this.countdown > 0) {
        this.countdown--;
      } else {
        clearInterval(this.timer);
      }
    }, 1000);
  }

 verifyOtp(): void {
    const email = this.signupForm.get('email')?.value;
    const otp = this.signupForm.get('otp')?.value;

    


    this.authService.verifyOtp(email, otp).subscribe({
      next: res => {
        console.log('OTP Verified:', res);
        if (res.includes('success')) {
          alert('OTP Verified Successfully');
          this.emailVerified = true;
          this.otpError = false;
        } else {
          this.otpError = true;
        }
      },
      error: err => {
        console.error('OTP verification failed:', err);
        alert('OTP Verification Failed');
        this.otpError = true;
      }
    });
  }



  goToLogin(): void {
    this.router.navigate(['/login']);
  }

}

// ✅ Custom validator: checks if password and confirm password match
// export function passwordMatchValidator(form: FormGroup) {
//   const password = form.get('password')?.value;
//   const confirmPassword = form.get('confirmpassword')?.value;
//   return password === confirmPassword ? null : { passwordMismatch: true };
// }
export function passwordMatchValidator(form: FormGroup) {
  const password = form.get('password');
  const confirmPassword = form.get('confirmpassword');

  if (password && confirmPassword && password.value !== confirmPassword.value) {
    confirmPassword.setErrors({ passwordMismatch: true });
  } else {
    // remove error if already fixed
    if (confirmPassword?.hasError('passwordMismatch')) {
      confirmPassword.setErrors(null);
    }
  }

  return null;
}
