import { Component, OnInit, signal } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';



import{ SearchComponent } from "./search/search";
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from './core/navbar/navbar';
import { AuthService } from './authservice';
import { CommonModule } from '@angular/common';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
@Component({
  selector: 'app-root',
  standalone:true,
  imports: [ HttpClientModule, RouterModule,NavbarComponent,CommonModule, ToastModule, ConfirmDialogModule
  ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  protected readonly title = signal('basicproject');

   showNavbar = false;


   showLayout = true;

  constructor(private router: Router, private authService: AuthService) {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        // Hide header/footer on login & signup
        this.showLayout = !(event.url.includes('/login') || event.url.includes('/signup'));
      }
    });
  }


  // constructor(private authService: AuthService) {}


  sidebarOpen = false;  // ✅ Add this property

  toggleSidebar() {
    this.sidebarOpen = !this.sidebarOpen;
  }

  closeSidebar() {
    this.sidebarOpen = false;
  }

  ngOnInit() {
    // Check login status on app load
    this.showNavbar = this.authService.isLoggedIn();
  }

  logout() {
    this.authService.logout();
    this.showNavbar = false;
  }
}
