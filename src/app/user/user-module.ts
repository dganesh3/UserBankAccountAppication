import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserComponent } from './user';
import { PaymentComponent } from './payment/payment';
import { ToastModule } from 'primeng/toast';
import { TransactionComponent } from './transaction/transaction';
@NgModule({

  imports: [
    CommonModule,
    UserComponent,
    ToastModule,
    TransactionComponent
  ]
})
export class UserModule { }
