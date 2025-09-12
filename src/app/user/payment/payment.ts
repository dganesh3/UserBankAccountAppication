import { Component, OnInit } from '@angular/core';
import {  PaymentService,  } from '../payment-service';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { FormBuilder, FormGroup, FormsModule, Validators } from '@angular/forms';
import { log } from 'console';
import { HttpClient } from '@angular/common/http';
    import Swal from 'sweetalert2';
import { Router } from '@angular/router';
@Component({
  selector: 'app-payment',
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './payment.html',
  styleUrls: ['./payment.css']
})
export class PaymentComponent implements OnInit {
 paymentForm!: FormGroup;
  users: any[] = [];
  accounts: any[] = [];
  actions: string[] = ['Deposit', 'Withdraw', 'Transfer'];
  selectedAction: string = '';
  history: any[] = [];
  showingHistory = false;

  constructor(private fb: FormBuilder, private paymentService: PaymentService, private router: Router) {}

  ngOnInit(): void {
    this.paymentForm = this.fb.group({
      userId: ['', Validators.required],
      action: ['', Validators.required],
      fromAccount: ['', Validators.required],
      toAccount: [''],
      amount: ['', [Validators.required, Validators.min(1)]]
    });

    this.loadUsers();
  }

 loadUsers() {
  this.paymentService.getUsers().subscribe((res: any) => {
    // If API returns { data: [...] }
    if (res?.data && Array.isArray(res.data)) {
      this.users = res.data;
    } 
    // If API directly returns an array
    else if (Array.isArray(res)) {
      this.users = res;
    } 
    // If API returns a single object
    else if (res?.data && typeof res.data === 'object') {
      this.users = [res.data];
    } 
    else {
      this.users = [];
    }

    console.log('Users loaded:', this.users);
  });
}


 onUserChange() {
  const userId = this.paymentForm.value.userId;
  if (!userId) {
    this.accounts = [];
    return;
  }

  this.paymentService.getAccounts(userId).subscribe((res: any) => {
    if (res?.data && Array.isArray(res.data)) {
      this.accounts = res.data;
    } else if (Array.isArray(res)) {
      this.accounts = res;
    } else if (res?.data && typeof res.data === 'object') {
      this.accounts = [res.data];
    } else {
      this.accounts = [];
    }

    console.log('Accounts loaded:', this.accounts);
  });
}


  onActionChange() {
    this.selectedAction = this.paymentForm.value.action;
  }

  submitPayment() {
    if (this.paymentForm.invalid) return;

    const { userId, fromAccount, toAccount, amount } = this.paymentForm.value;
    const user = this.users.find(u => u.id == userId);
    const fromAcc = this.accounts.find(a => a.id == fromAccount);
    const toAcc = this.accounts.find(a => a.id == toAccount);

    let action$;

    if (this.selectedAction === 'Deposit') {
      action$ = this.paymentService.deposit(fromAcc, user, amount);
    } else if (this.selectedAction === 'Withdraw') {
      action$ = this.paymentService.withdraw(fromAcc, user, amount);
    } else if (this.selectedAction === 'Transfer' && toAcc) {
      action$ = this.paymentService.transfer(fromAcc, user, toAcc, user, amount);
    }

    if (action$) {
      action$.subscribe(res => {
        alert(`✅ ${this.selectedAction} successful!`);
        this.paymentForm.reset();
        console.log('Payment successful:', res);

       
      });
    }
  }

  showHistory() {
   this.router.navigate(['/transaction']);
  }
}
