import { ChangeDetectorRef, Component, NgZone, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { CoreModule } from '../../core/core-module';
import { HttpClientModule } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { getLocalStorageItem, setLocalStorageItem } from '../../core/services/storage.util';
import { AuthService } from '../../authservice';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterModule, HttpClientModule]
})
export class LoginComponent {
  loginForm: FormGroup;
  submitted = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService,

  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

    onLogin(): void {
    this.submitted = true;
      
    if (this.loginForm.invalid) {
      Swal.fire({ icon: 'warning', title: 'Missing Fields', text: 'Enter both username and password' });
      return;
    }

    const { username, password } = this.loginForm.value;

    this.authService.login(username, password).subscribe({
      next: (res: any) => {
        if (res && res.username) {
          Swal.fire({ icon: 'success', title: 'Login Successful', text: `Welcome, ${res.firstname}!` })
            .then(() => this.router.navigate(['/user']));
        } else {
          Swal.fire({ icon: 'error', title: 'Login Failed', text: 'Invalid credentials!' });
        }
      },
      error: err => {
        console.error('Login error:', err);
        Swal.fire({ icon: 'error', title: 'Login Failed', text: 'Server error or invalid credentials!' });
      }
    });
  }
}


