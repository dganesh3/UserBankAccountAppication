import { ChangeDetectorRef, Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';
import { MatListModule } from '@angular/material/list';
import { AuthService } from '../../authservice';
@Component({
  selector: 'app-navbar',
  imports: [MatToolbarModule,
    MatIconModule,
    MatSidenavModule,
    MatButtonModule, RouterModule, MatListModule, FormsModule, CommonModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class NavbarComponent implements OnInit {
  isLoggedIn = false;
  sidebarOpen = false;

  constructor(public authService: AuthService, private router: Router) {}

  ngOnInit() {
    // Subscribe to auth changes
    this.authService.loggedIn$.subscribe(status => {
      console.log('Navbar sees loggedIn =', status);
      this.isLoggedIn = status;
    });
  }

  toggleSidebar() {
    this.sidebarOpen = !this.sidebarOpen;
  }

  closeSidebar() {
    this.sidebarOpen = false;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }


}
