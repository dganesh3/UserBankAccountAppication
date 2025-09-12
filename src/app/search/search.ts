// ✅ FINAL CODE for editing User + Bank in same component (inline)
// File: search.component.ts (standalone component version)

import { Component, OnInit, AfterViewInit, ViewChild, ViewEncapsulation, Injectable } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';

import { Router } from '@angular/router';

import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { Userservice } from '../core/services/userservice';

import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { PaginatorModule, PaginatorState } from 'primeng/paginator';
import { TreeNode } from 'primeng/api';
import { TreeTableModule } from 'primeng/treetable';


@Component({
 
  selector: 'app-search',
  standalone: true,

  templateUrl: './search.html',
  styleUrls: ['./search.css'],
  imports: [
    CommonModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    PaginatorModule,
    TreeTableModule,
    
  
  ],
})
export class SearchComponent implements OnInit {
  users: any[] = ['firstName', 'lastName', 'email', 'mobile', 'country','state','options'];
  filteredUsers: any[] = [];
  searchKeyword = '';
  Array: any;

  totalRecords: number = 0;
  rows = 10;
  constructor(
    private userService: Userservice,
    private router: Router,
  

  ) { }

 ngOnInit(): void {
  // Load total count first
  this.userService.getUsersCount().subscribe(count => {
    this.totalRecords = count;
    console.log("Total users:", count);

    // Load first page (0 index)
    this.onPageChange({ first: 0, rows: this.rows });
  });
}

   
  getAllUsers(): void {
    this.userService.getAllUsers().subscribe({
      next: (res: any) => {
        console.log('✅ Raw response:', res);

        // Check if res is { data: [] }
        if (Array.isArray(res)) {
          this.users = res;
          this.filteredUsers = res;
        } else if (res && Array.isArray(res.data)) {
          this.users = res.data;
          this.filteredUsers = res.data;
        } else {
          console.error(' Unexpected response shape:', res);
          this.users = [];
          this.filteredUsers = [];
        }
      },
      error: (err) => {
        console.error(' Failed to fetch users', err);
        Swal.fire('Error', 'Failed to load user data', 'error');
      }
    });
  }


onPageChange(event: any) {
  const start = event.first;              // event.first is 0-based index
  const end = event.first + event.rows;   // calculate end index

  console.log(`Fetching users from ${start} to ${end}`);

  this.userService.getUsers(start, end).subscribe(res => {
    this.users = res;
    this.filteredUsers = res;
    console.log("Loaded users:", res);
  });
}





  get safeFilteredUsers(): any[] {
    return Array.isArray(this.filteredUsers) ? this.filteredUsers : [];
  }
  getUserById(id: number): void {

    this.userService.getUserById(id).subscribe({
      next: (res: any) => {
        console.log('✅ User details:', res);
        // Handle user details
      },
      error: (err) => {
        console.error(' Failed to fetch user details', err);
        Swal.fire('Error', 'Failed to load user details', 'error');
      }
    });
  }

  applyFilter(): void {
    const keyword = this.searchKeyword.trim().toLowerCase();
    this.filteredUsers = this.users.filter(user =>
      user.firstName?.toLowerCase().includes(keyword) ||
      user.lastName?.toLowerCase().includes(keyword) ||
      user.email?.toLowerCase().includes(keyword) ||
      // user.mobile?.toLowerCase().includes(keyword) ||
      user.country?.toLowerCase().includes(keyword)
    );
  }

  navigateToCreateUser(): void {
    this.router.navigate(['/user']);
  }

  editUser(id: number): void {
    this.router.navigate(['/user'],
      {
        queryParams:
        {
          id: id,
          mode: 'edit'
        }
      });
  }

  confirmAndDeleteUser(id: number): void {
    debugger
    Swal.fire({
      title: 'Are you sure?',
      text: 'This user will be permanently deleted!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#e53935',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Yes, delete it!'
    }).then(result => {
      if (result.isConfirmed){
         this.deleteUser(id);
      };
    });
  }




  private deleteUser(id: number): void {
    this.userService.deleteUser(id).subscribe({
      next: () => {
        Swal.fire({
          title: 'Deleted!',
          text: 'User has been deleted.',
          icon: 'warning',
          confirmButtonText: 'Confirm',
          confirmButtonColor: '#3085d6',
          showCancelButton: true,
        
          cancelButtonColor: '#e53935'
        })
        .then(() => {
          this.getAllUsers();
        });
      },
      error: (err) => {
        console.error('Deletion failed', err);
        Swal.fire('Error', 'Failed to delete user', 'error');
      }
    });
  }
  
}