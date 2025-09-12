import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';


export interface Transaction {
  id?: number;
  type: 'Deposit' | 'Withdraw' | 'Transfer';
  amount: number;

  // From account details
  accountId: number;
  accountNumber: Number;
  holderName: string;
  bankName: string;

  // To account details (only for transfer)
  toAccountId?: number;
  toAccountNumber?: Number;
  toHolderName?: string;
  toBankName?: string;

  timestamp?: string;
}

@Injectable({
  providedIn: 'root'
})
export class PaymentService
 {
 
  
 

  // Get all users with accounts
  getUsers(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:9090/users/all');
  }

    private apiUrl = 'http://localhost:9090';

    // Payment microservice

     private baseUrl = 'http://localhost:9090';
  constructor(private http: HttpClient) {}

  getAccounts(userId: number) {
    return this.http.get<any>(`${this.baseUrl}/payments/accounts`);
  }
   getAccountsByUserId(userId: any) {
    return this.http.get<any>(`${this.baseUrl}/payments/users/${userId}/accounts`);
  }

 
deposit(account: any, user: any, amount: number) {
  const tx: Transaction = {
    type: 'Deposit',
    amount,
    accountId: account.id,
    accountNumber: account.accountNumber,
    holderName: `${user.firstName} ${user.lastName}`,
    bankName: account.bankName
  };
  // ✅ PathVariable for accountId
  return this.http.post(`${this.baseUrl}/payments/${account.id}/deposit`, tx);
}

withdraw(account: any, user: any, amount: number) {
  const tx: Transaction = {
    type: 'Withdraw',
    amount,
     accountId: account.id,
    accountNumber: account.accountNumber,
    holderName: `${user.firstName} ${user.lastName}`,
    bankName: account.bankName
  };
  // ✅ PathVariable for accountId
  return this.http.post(`${this.baseUrl}/payments/${account.id}/withdraw`, tx);
}

transfer(fromAcc: any, fromUser: any, toAcc: any, toUser: any, amount: number) {
console.log('TRANSFER CALL', fromAcc, toAcc, fromAcc?.id, toAcc?.id);
  if (!fromAcc?.id || !toAcc?.id) {
     console.error('fromAcc or toAcc ID is missing', { fromAcc, toAcc });
    return; //
  }
  const tx: Transaction = {
  type: 'Transfer',
  amount,
  accountId: fromAcc.id,
  accountNumber: Number(fromAcc.accountNumber),
  holderName: fromAcc.accountHolderName,
  bankName: fromAcc.bankName,

  toAccountId: toAcc.id,
  toAccountNumber: Number(toAcc.accountNumber),
  toHolderName: toAcc.accountHolderName,
  toBankName: toAcc.bankName,

  timestamp: new Date().toISOString()      // backend LocalDateTime
  };
console.log('Transfer DTO', tx);
  //  return this.http.post(`${this.apiUrl}/transactions/save`, tx);
 return this.http.post(`${this.baseUrl}/payments/transfer`, tx);
}


  getHistory(): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.apiUrl}/transactions/history`);
  }

  getAllTransactionHistory(startIndex: number,lastIndex: number): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.apiUrl}/transactions/getAllTransactionHistory/${startIndex}/${lastIndex}`);
  }

  getTransactionCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/transactions/count`);
  }

}
