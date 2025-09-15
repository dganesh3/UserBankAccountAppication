import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, Inject } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';
import { PLATFORM_ID } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
private baseUrl = 'http://localhost:9090/api/auth';
  private loggedIn = new BehaviorSubject<boolean>(this.hasToken());
  loggedIn$ = this.loggedIn.asObservable();

  constructor(private http: HttpClient) {}

  private hasToken(): boolean {
    return typeof window !== 'undefined' && localStorage.getItem('token') !== null;
  }

  /** 🔹 Send OTP */
  sendOtp(email: string): Observable<any> {
    const params = new HttpParams().set('email', email);
    return this.http.post(`${this.baseUrl}/send-otp`, {}, { params, responseType: 'text' });
  }

  /** 🔹 Verify OTP */
  verifyOtp(email: string, otp: string): Observable<any> {
    const params = new HttpParams().set('email', email).set('otp', otp);
    return this.http.post(`${this.baseUrl}/verify-otp`, {}, { params, responseType: 'text' });
  }

  /** 🔹 Register user after OTP verification */
  register(signupData: any, otp: string): Observable<any> {
    const params = new HttpParams().set('otp', otp);
    return this.http.post(`${this.baseUrl}/register`, signupData, { params });
  }

  /** 🔹 Login (username/email + password) */
 login(usernameOrEmail: string, password: string): Observable<any> {
    const body = { usernameOrEmail, password };

    return this.http.post(`${this.baseUrl}/login`, body).pipe(
      tap((res: any) => {
        if (res && res.username) {
          localStorage.setItem('token', 'dummy-token'); // optional placeholder
          this.loggedIn.next(true);
        }
      })
    );
  }
  /** 🔹 Logout */
  logout(): void {
    localStorage.removeItem('token');
    this.loggedIn.next(false);
    window.location.reload();
  }

  /** 🔹 Auth restore on reload */
  restoreAuth(): Promise<void> {
    return new Promise(resolve => {
      const token = localStorage.getItem('token');
      this.loggedIn.next(!!token);
      resolve();
    });
  }

  isLoggedIn(): boolean {
    return this.loggedIn.value;
  }


  checkEmailExists(email: string): Observable<boolean> {
    const params = new HttpParams().set('email', email);
    return this.http.get<boolean>(`${this.baseUrl}/check-email`, { params });
  }

}
