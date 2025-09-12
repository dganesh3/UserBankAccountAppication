import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Validators, FormBuilder, FormsModule, EmailValidator } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import Swal from 'sweetalert2'
@Component({
  selector: 'app-support',
  imports: [ReactiveFormsModule, FormsModule, CommonModule],
  templateUrl: './support.html',
  styleUrl: './support.css'
})
export class SupportComponent {

  supportForm: any;

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.supportForm = this.fb.group({
      name: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
      mobile: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]],
      reason: ['', Validators.required]
    });
  }



  onSubmit() {
    if (this.supportForm.valid) {
      this.http.post('http://localhost:8080/support', this.supportForm.value)
        .subscribe({
          next: () => {
            Swal.fire({
              title: 'Notice',
              text: 'This is your user alert message.',
              icon: 'info',
              confirmButtonText: 'OK'
            });
            this.supportForm.reset();
          },
          error: (err) => console.error('Error sending support request', err)
        });
    }
  }
}
