import { Component, OnInit } from '@angular/core';
import { PaymentService, Transaction } from '../payment-service';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { PaginatorModule } from 'primeng/paginator';
import { Router } from '@angular/router';

@Component({
  selector: 'app-transaction',
  imports: [CommonModule, TableModule, PaginatorModule],
  templateUrl: './transaction.html',
  styleUrls: ['./transaction.css']
})
export class TransactionComponent implements OnInit {
 allTransactions: Transaction[] = [];   // Full data
  transactions: Transaction[] = [];      // Current page
  totalRecords: number = 0;

  constructor(private paymentService: PaymentService,private router: Router) {}

  ngOnInit(): void {
  this.onPageChange({ first: 0, rows: 10 }); // load first page
}

onPageChange(event: any) {
  const start = event.first;
  const end = event.first + event.rows;

  console.log(`Fetching transactions from ${start} to ${end}`);

  this.paymentService.getAllTransactionHistory(start, end).subscribe(data => {
    this.transactions = data;
    this.paymentService.getTransactionCount().subscribe(count => {
      this.totalRecords = count;
      console.log(`Total transactions count: ${count}`);
    });
    console.table(data, [`Loaded transactions from ${start} to ${end}`]);
    // data.forEach(x => console.log(x));
  });
}

onBack() {
  this.router.navigate(['/payment']);
}
}