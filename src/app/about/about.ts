import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';

@Component({
  selector: 'app-about',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatListModule],
  template: `
    <mat-card>
      <mat-card-title>{{ appInfo.name }}</mat-card-title>
      <mat-card-content>
        <h5>{{ appInfo.description }}</h5>

        <h3>Technologies:</h3>
        <mat-list>
          <mat-list-item *ngFor="let tech of appInfo.technologies">{{ tech }}</mat-list-item>
        </mat-list>

        <h3>Features:</h3>
        <mat-list>
          <mat-list-item *ngFor="let feature of appInfo.features">{{ feature }}</mat-list-item>
        </mat-list>

        <p><strong>Purpose:</strong> {{ appInfo.purpose }}</p>
        <p><strong>Author:</strong> {{ appInfo.author }}</p>
      </mat-card-content>
    </mat-card>
  `,
  styles: [`
    mat-card { margin: 20px; padding: 20px; }
    mat-card-title { font-size: 24px; font-weight: bold; }
    h3 { margin-top: 20px; }
    mat-list-item { font-size: 16px; }
  `]
})
export class AboutComponent {
  appInfo = {
    name: 'UserBankApplication',
    description: `This application manages Users, their Contacts, Banks, and Accounts. 
                  It provides CRUD operations and ensures data integrity with validation.`,
    technologies: ['Angular', 'Spring Boot', 'MySQL', 'HTML', 'CSS', 'TypeScript'],
    features: [
      'User registration and management',
      'Bank account creation and management',
      'Contact management',
      'Excel import for contacts',
      'Profile image upload',
      'Dependent dropdowns for country/state/district'
    ],
    purpose: 'To create a fully functional banking and contact management system for internal use.',
    author: 'D. Ganesh'
  };
}
