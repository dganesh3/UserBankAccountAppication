import { Component, OnInit, TemplateRef, ViewChild, ChangeDetectorRef, ElementRef } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Userservice } from '../core/services/userservice';
import { ViewEncapsulation } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Locationservice } from '../core/services/locationservice';
import { debounceTime, distinctUntilChanged, map, of, switchMap } from 'rxjs';
import { RouterModule } from '@angular/router';

import * as ExcelJS from 'exceljs';
import { saveAs } from 'file-saver';


import Swal from 'sweetalert2';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../shared/shared-module';
import { MatDialog } from '@angular/material/dialog';
import * as XLSX from 'xlsx';


@Component({
  selector: 'app-dashboard',
  templateUrl: './user.html',
  styleUrls: ['./user.css'],
  standalone: true,
  imports: [HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    CommonModule,
    SharedModule,
    RouterModule
  ],
  encapsulation: ViewEncapsulation.None
})
export class UserComponent implements OnInit {
  dashboardForm!: FormGroup;
  accountDialogRef: any;
  selectedBankIndex = 0;
  editMode = false;
  selectedUserId = 0;
  contactsPreviewData: any[] = [];
  previewData: any[] = [];

  @ViewChild('accountDialogTemplate') accountDialogTemplate!: TemplateRef<any>;
  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;
  @ViewChild('contactsPreviewTemplate') contactsPreviewTemplate: any;
  // countries: string[] = [];
  // states: string[] = [];
  // districts: string[] = [];
  // filteredCountries!: Observable<string[]>;
  // filteredStates!: Observable<string[]>;
  // filteredDistricts!: Observable<string[]>;

  // Master data (from backend)
  countries: { id: number; name: string }[] = [];
  states: { id: number; name: string }[] = [];
  districts: { id: number; name: string; pinCode?: string }[] = [];

  // Autocomplete lists (string names only)
  filteredCountries: string[] = [];
  filteredStates: string[] = [];
  filteredDistricts: string[] = [];



  bankTypes: string[] = ['Public', 'Private', 'Foreign'];
  accountTypes: string[] = ['Savings', 'Current', 'Fixed'];

  relationTypes: string[] = ['Father', 'Mother', 'Brother', 'Sister', 'Spouse', 'Friend', 'Colleague', 'Other'];


  constructor(
    private http: HttpClient,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private dialog: MatDialog,
    private userService: Userservice,
    private changeDetector: ChangeDetectorRef,
    private locationService: Locationservice
  ) { }

  // ngOnInit(): void {
  //   this.initForm();

  //   const queryParams = this.route.snapshot.queryParamMap;
  //   const id = queryParams.get('id');
  //   const mode = queryParams.get('mode');

  //   this.editMode = mode === 'edit';

  //   if (id && this.editMode) {
  //     this.selectedUserId = +id;
  //     this.loadUserForEdit(this.selectedUserId);
  //   }

  //   if (!this.editMode && this.banks.length === 0) {
  //     this.addBank();
  //   }
  //   if (this.contacts.length === 0) {
  //     this.addContact();
  //   }

  // Load countries
  // this.locationService.getCountries().subscribe((countries: { code: string; name: string; }[]) => {
  //   this.countries = countries.map(country => country.name);
  // });

  // 🔹 When country changes → load states
  // this.dashboardForm.get('country')!.valueChanges.subscribe(country => {
  //   if (country) {
  //     this.locationService.getStates(country).subscribe((states: string[]) => {
  //       this.states = states;
  //       this.dashboardForm.get('state')!.reset('');
  //       this.dashboardForm.get('district')!.reset('');
  //     });
  //   } else {
  //     this.states = [];
  //     this.districts = [];
  //   }
  // });

  // When state changes → load districts
  // this.dashboardForm.get('state')!.valueChanges.subscribe(state => {
  //   const country = this.dashboardForm.get('country')!.value;
  //   if (country && state) {
  //     this.locationService.getDistricts(country, state).subscribe((districts: string[]) => {
  //       this.districts = districts;
  //       this.dashboardForm.get('district')!.reset('');
  //     });
  //   } else {
  //     this.districts = [];
  //   }
  // });

  // this.dashboardForm.get('district')!.valueChanges.subscribe(districtName => {
  //   const countryName = this.dashboardForm.get('country')?.value;
  //   const stateName = this.dashboardForm.get('state')?.value;

  //   if (countryName && stateName && districtName) {
  //     this.locationService.getPincode(countryName, stateName, districtName)
  //       .subscribe(pincode => {
  //         this.dashboardForm.get('pincode')?.setValue(pincode);
  //       });
  //   }
  // });


  // Filters
  //   this.filteredCountries = this.dashboardForm.get('country')!.valueChanges.pipe(
  //     startWith(''),
  //     map(value => this._filter(value, this.countries))
  //   );

  //   this.filteredStates = this.dashboardForm.get('state')!.valueChanges.pipe(
  //     startWith(''),
  //     map(value => this._filter(value, this.states))
  //   );

  //   this.filteredDistricts = this.dashboardForm.get('district')!.valueChanges.pipe(
  //     startWith(''),
  //     map(value => this._filter(value, this.districts))
  //   );
  // }

  // private _filter(value: string, options: string[]): string[] {
  //   const filterValue = value?.toLowerCase() || '';
  //   return options.filter(option => option.toLowerCase().includes(filterValue));
  // }


  ngOnInit(): void {
  this.initForm();

  const queryParams = this.route.snapshot.queryParamMap;
  const id = queryParams.get('id');
  const mode = queryParams.get('mode');
  this.editMode = mode === 'edit';
  if (id && this.editMode) {
    this.selectedUserId = +id;
  }

  if (!this.editMode && this.banks.length === 0) {
    this.addBank();
  }
  if (this.contacts.length === 0) {
    this.addContact();
  }

  // --- Load countries first ---
  this.locationService.getAllCountries().subscribe({
    next: (data: any) => {
      const list = data.countries || data;
      this.countries = list.map((c: any) => ({ id: c.id, name: c.name }));
      this.filteredCountries = this.countries.map(c => c.name);

      // 🔹 Only now load edit user
      if (this.editMode && this.selectedUserId) {
        this.loadUserForEdit(this.selectedUserId);
      }
    },
    error: err => console.error('Error loading countries:', err)
  });

  // --- Country change -> load states ---
  this.dashboardForm.get('country')!.valueChanges.pipe(
    debounceTime(200),
    distinctUntilChanged(),
    switchMap(countryName => {
      if (!countryName) return of([]);

      const countryObj = this.countries.find(c => c.name === countryName);
      if (!countryObj) return of([]);
      return this.locationService.getStatesByCountry(countryObj.id).pipe(
        map((res: any) => {
          const states = res.stateDtos || res;
          return states.map((s: any) => ({ id: s.id, name: s.stateName || s.name }));
        })
      );
    })
  ).subscribe(states => {
    this.states = states;
    this.filteredStates = states.map((s: { name: any; }) => s.name);

    // ⚠️ Only reset if not editMode
    if (!this.editMode) {
      this.dashboardForm.get('state')!.reset('');
      this.dashboardForm.get('district')!.reset('');
    }
  });

  // --- State change -> load districts ---
  this.dashboardForm.get('state')!.valueChanges.pipe(
    debounceTime(200),
    distinctUntilChanged(),
    switchMap(stateName => {
      if (!stateName) return of([]);
      const stateId = this.states.find(s => s.name === stateName)?.id;
      if (!stateId) return of([]);
      return this.locationService.getDistrictsByState(stateId).pipe(
        map((districts: any[]) => districts.map(d => ({
          id: d.id,
          name: d.districtName || d.name,
          pinCode: d.pinCode?.code || ''
        })))
      );
    })
  ).subscribe(districts => {
    this.districts = districts;
    this.filteredDistricts = districts.map(d => d.name);

    if (!this.editMode) {
      this.dashboardForm.get('district')!.reset('');
    }
  });

  // --- District change -> load pincode ---
  this.dashboardForm.get('district')?.valueChanges.pipe(
    debounceTime(200),
    distinctUntilChanged(),
    switchMap(districtName => {
      const selected = this.districts.find(d => d.name === districtName);
      if (!selected?.id) return of({ code: '' });
      return this.locationService.getPincodeByDistrict(selected.id);
    })
  ).subscribe(pincode => {
    this.dashboardForm.get('pincode')?.setValue(pincode.code || '');
  });
}




  // --- Filter helper ---
  private _filter(value: string, options: string[]): string[] {
    const filterValue = value?.toLowerCase() || '';
    return options.filter(option => option.toLowerCase().includes(filterValue));
  }



  defaultImage = 'https://cdn-icons-png.flaticon.com/512/847/847969.png';
  profileImageUrl: string | ArrayBuffer | null = this.defaultImage;
  selectedImage: File | null = null;

  onImageSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    const file = input.files[0];

    // ✅ File size check (10MB limit)
    const maxSizeInMB = 20;
    const maxSizeInBytes = maxSizeInMB * 1024 * 1024;

    if (file.size > maxSizeInBytes) {
      Swal.fire({
        icon: 'error',
        title: 'File Too Large',
        text: `Selected image exceeds ${maxSizeInMB}MB. Please choose a smaller file.`
      });
      input.value = ''; // Clear the file input
      this.selectedImage = null;
      this.profileImageUrl = this.defaultImage;
      return;
    }

    this.selectedImage = file;

    const reader = new FileReader();
    reader.onload = () => {
      this.profileImageUrl = reader.result;
    };
    reader.readAsDataURL(file);
  }

  initForm(): void {
    this.dashboardForm = this.fb.group({
      id: [''],
      firstName: ['', [Validators.required]],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      mobile: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/), Validators.minLength(10), Validators.maxLength(10)]],
      address: ['', Validators.required],
      pincode: ['', Validators.required],
      state: ['', Validators.required],
      country: ['', Validators.required],
      district: ['', Validators.required],
      banks: this.fb.array([]),
      contacts: this.fb.array([
        this.createContactFormGroup()
      ])
    });
  }
  createContactFormGroup(): FormGroup {

    return this.fb.group({

      id: [''],
      name: ['', Validators.required],
      relation: ['', Validators.required],
      mobile: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/), Validators.minLength(10), Validators.maxLength(10)]],
      email: ['', [Validators.required, Validators.email]],
      address: ['', Validators.required],
      deleted: [false]
    });
  }


  get contacts(): FormArray {
    return this.dashboardForm.get('contacts') as FormArray;
  }
  addContact(): void {
    if (!this.isLastRowFilled(this.contacts, ['name', 'mobile'])) {
      alert('Please fill the current contact row before adding a new one.');
      return;
    }
    this.contacts.push(this.createContactFormGroup());
  }

  removeContact(index: number) {
    this.removeItem(this.contacts, index);
  }

  undoContact(index: number): void {
    const contact = this.contacts.at(index);
    if (contact) {
      contact.get('deleted')?.setValue(false);
    }
  }
  confirmContacts(): void {
    console.log("Confirmed contacts:", this.contacts.value);
    // Optionally mark as touched, validate, etc.
  }







  get banks(): FormArray {
    return this.dashboardForm.get('banks') as FormArray;
  }



  createBankGroup(): FormGroup {
    return this.fb.group({
      id: [''], // 
      bankName: ['', Validators.required],
      bankType: ['', Validators.required],
      branchName: ['', Validators.required],
      bankAddress: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/), Validators.minLength(10), Validators.maxLength(10)]],
      managerName: ['', Validators.required],
      establishedDate: ['', Validators.required],
      deleted: [false],
      accounts: this.fb.array([this.createAccountGroup()])
    });
  }

  createAccountGroup(): FormGroup {
    return this.fb.group({
      id: [''], // Account ID
      accountHolderName: ['', Validators.required],
      accountNumber: ['', [
        Validators.required,
        Validators.pattern(/^\d{12,18}$/)  // 12 to 18 digits only
      ], Validators.minLength(12), Validators.maxLength(18)],
      accountType: ['', Validators.required],
      ifscCode: ['', Validators.required],
      bankName: ['', Validators.required],
      branchName: ['', Validators.required],
      bankAddress: ['', Validators.required],
      balance: ['', Validators.required],
      deleted: [false]
    });
  }


  allowOnlyNumbers(event: KeyboardEvent) {
    const charCode = event.which ? event.which : event.keyCode;
    if (charCode < 48 || charCode > 57) {
      event.preventDefault(); // blocks non-numeric input
    }
  }

  getAccounts(bankIndex: number): FormArray {
    return this.banks.at(bankIndex).get('accounts') as FormArray;
  }

  addBank(): void {
    if (!this.isLastRowFilled(this.banks, ['bankName', 'bankType', 'branchName'])) {
      alert('Please fill the current bank row before adding a new one.');
      return;
    }
    this.banks.push(this.createBankGroup());
  }
  private isLastRowFilled(formArray: FormArray, requiredFields: string[]): boolean {
    if (!formArray.length) return true; // No rows yet — allow add
    const lastGroup = formArray.at(formArray.length - 1) as FormGroup;

    return requiredFields.every(field => {
      const value = lastGroup.get(field)?.value;
      return value !== null && value !== undefined && value.toString().trim() !== '';
    });
  }


  addAccount(bankIndex: number): void {
    const accounts = this.getAccounts(bankIndex);
    if (!this.isLastRowFilled(accounts, ['accountHolderName', 'accountNumber', 'accountType'])) {
      alert('Please fill the current account row before adding a new one.');
      return;
    }
    accounts.push(this.createAccountGroup());
  }

  removeItem(formArray: FormArray, index: number): void {
    const group = formArray.at(index) as FormGroup;
    if (!group) return;

    // If only one item, just reset instead of removing
    if (formArray.length === 1) {
      group.reset({ deleted: false });
      return;
    }

    // Check if form group is empty (ignoring 'deleted' field)
    const isEmpty = this.isFormGroupEmpty(group);

    if (isEmpty) {
      formArray.removeAt(index); // Hard delete
    } else {
      group.get('deleted')?.setValue(true); // Soft delete
    }
  }


  // Recursively checks if a FormGroup is empty

  private isFormGroupEmpty(group: FormGroup): boolean {
    return Object.keys(group.controls).every(key => {
      if (key === 'deleted') return true;

      const control = group.get(key);

      if (control instanceof FormGroup) {
        return this.isFormGroupEmpty(control);
      }

      if (control instanceof FormArray) {
        return control.controls.every(c =>
          c instanceof FormGroup ? this.isFormGroupEmpty(c) : !this.hasValue(c.value)
        );
      }

      return !this.hasValue(control?.value);
    });
  }

  // Simple helper to check if a value is "filled"
  private hasValue(value: any): boolean {
    return value !== null && value !== '' && value !== false;
  }


  removeBank(index: number) {
    this.removeItem(this.banks, index);
  }


  undoBank(index: number): void {
    const bankGroup = this.banks.at(index);
    if (bankGroup) {
      bankGroup.get('deleted')?.setValue(false); // Undo soft delete
      // console.log(`Bank at index ${index} restored`);
    }
  }





  onCancel(): void {
    // this.dashboardForm.reset();
    // this.banks.clear();
    // this.addBank(); // restore 1 default bank row
    this.router.navigate(['/search']);
  }

  removeAccount(bankIndex: number, accountIndex: number) {
    console.log('Banks length:', this.banks.length, 'bankIndex:', bankIndex);
    const bankGroup = this.banks.at(bankIndex);
    if (!bankGroup) {
      console.error('Bank group not found for index', bankIndex);
      return;
    }

    const accountsFA = bankGroup.get('accounts') as FormArray;
    if (!accountsFA) {
      console.error('No accounts FormArray in bank at index', bankIndex, bankGroup.value);
      return;
    }

    this.removeItem(accountsFA, accountIndex);
  }



  undoAccount(index: number): void {
    const accounts = this.getAccounts(this.selectedBankIndex);
    const accountGroup = accounts.at(index);
    if (accountGroup) {
      accountGroup.get('deleted')?.setValue(false);
    }
  }


  openAccountDialog(index: number, templateRef: any): void {
    console.log("Opening dialog for bank index:", index);
    this.selectedBankIndex = index;

    // ✅ Fix: Set proper focus management
    this.accountDialogRef = this.dialog.open(templateRef, {
      autoFocus: true,
      restoreFocus: true,
      disableClose: true,
      width: '90vw',               // 95% of screen width
      maxWidth: '1000px',          // Don’t let it grow beyond this
      height: 'auto',
      maxHeight: '85vh',
      panelClass: 'custom-dialog-container'

    });

    // ✅ Additional fallback in case dialog doesn't restore focus correctly
    setTimeout(() => document.body.focus(), 100);
  }



  closeAccountDialog(): void {
    if (this.accountDialogRef) {
      this.accountDialogRef.close();
    }
  }

  confirmAccountDialog(): void {
    this.accountDialogRef?.close();
  }


  onCountryChange(event: any): void {
    const country = event.target.value;
    // this.filteredStates = this.statesByCountry[country] || [];
    this.dashboardForm.get('state')?.setValue('');
  }


  onSubmit(): void {
    if (this.dashboardForm.invalid) {
      this.dashboardForm.markAllAsTouched();
      Swal.fire({
        icon: 'error',
        title: 'Invalid Form',
        text: 'Please fix the errors before submitting.'
      });
      return;
    }

    const formData = this.dashboardForm.value;


    // Format establishedDate to YYYY-MM-DD
    formData.banks.forEach((bank: any) => {
      if (bank.establishedDate instanceof Date) {
        bank.establishedDate = bank.establishedDate.toISOString().split('T')[0];
      }
    });
    let action = "create";

    if (this.editMode)
      action = "edit";

    // // 📦 Select API call with image handling
    // const apiCall = this.editMode
    //   ? this.userService.updateUser(this.selectedUserId, formData, this.selectedImage)
    //   : this.userService.saveUser(formData, action);

    this.userService.saveUser(formData, action).subscribe({
      next: (res) => {
        // alert(res)
        Swal.fire({
          icon: 'success',
          title: this.editMode ? 'updated' : 'created',
          text: 'User succefuly submitted',
          timer: 2000,
          showConfirmButton: false
        }).then(() => {

          setTimeout(() => {
            this.router.navigate(['/search']);
            document.body.focus();

          }, 0);
        })
      }
    })


    //   apiCall.subscribe({
    //     next: (res) => {
    //       const id = res?.data?.id || this.selectedUserId;
    //       Swal.fire({
    //   icon: 'success',
    //   title: this.editMode ? 'Updated!' : 'Created!',
    //   text: `User successfully ${this.editMode ? 'updated' : 'created'} with ID: ${id}`,
    //   timer: 2000,
    //   showConfirmButton: false
    // }).then(() => {
    //   // 🔥 Wrap navigation/focus logic to avoid aria-hidden trap
    //   setTimeout(() => {
    //     this.router.navigate(['/search']);
    //     document.body.focus();
    //   }, 0);
    // });
    //     },
    //     error: (err) => {
    //       const msg = err?.error?.message || 'Unexpected error occurred.';
    //       Swal.fire({
    //         icon: 'error',
    //         title: 'Error',
    //         text: `Submission failed: ${msg}`
    //       });
    //       console.error(err);
    //     }
    //   });
  }

 loadUserForEdit(id: number): void {
  this.userService.getUserById(id).subscribe({
    next: (response: any) => {
      const userData = response?.data;
      if (!userData) return;

      // Reset banks & contacts (as you already have)
      this.banks.clear();
      userData.banks?.forEach((bank: any) => {
        const bankGroup = this.createBankGroup();
        const accountsArray = bankGroup.get('accounts') as FormArray;
        accountsArray.clear();
        bank.accounts?.forEach(() => accountsArray.push(this.createAccountGroup()));
        this.banks.push(bankGroup);
      });

      this.contacts.clear();
      userData.contacts?.forEach((contact: any) => {
        this.contacts.push(this.fb.group({
          id: [contact.id],
          name: [contact.name],
          relation: [contact.relation],
          mobile: [contact.mobile],
          email: [contact.email],
          address: [contact.address]
        }));
      });

      // Basic patch (without location yet)
      this.dashboardForm.patchValue({
        id: userData.id || '',
        firstName: userData.firstName || '',
        lastName: userData.lastName || '',
        email: userData.email || '',
        mobile: userData.mobile || '',
        address: userData.address || ''
      });

      // Profile image
      const imagePath = userData.profileImage;
      this.profileImageUrl = imagePath
        ? (imagePath.startsWith('http') || imagePath.startsWith('/uploads')
          ? `http://localhost:8080${imagePath}`
          : this.defaultImage)
        : this.defaultImage;

      // 🔥 Cascade load location fields
      const selectedCountry = this.countries.find(c => c.name === userData.country);
      if (selectedCountry?.id) {
        this.locationService.getStatesByCountry(selectedCountry.id).subscribe(states => {
          this.states = states.map((s: any) => ({ id: s.id, name: s.stateName || s.name }));
          this.filteredStates = this.states.map(s => s.name);

          this.dashboardForm.get('country')?.setValue(userData.country, { emitEvent: false });
          this.dashboardForm.get('state')?.setValue(userData.state, { emitEvent: false });

          const selectedState = this.states.find(s => s.name === userData.state);
          if (selectedState?.id) {
            this.locationService.getDistrictsByState(selectedState.id).subscribe(districts => {
              this.districts = districts.map((d: any) => ({
                id: d.id,
                name: d.districtName || d.name,
                pinCode: d.pinCode?.code || ''
              }));
              this.filteredDistricts = this.districts.map(d => d.name);

              this.dashboardForm.get('district')?.setValue(userData.district, { emitEvent: false });

              // ✅ Directly patch pincode from backend
              this.dashboardForm.get('pincode')?.setValue(userData.pincode || '');
            });
          }
        });
      }
      this.populateBankForm(userData.banks || []);
        this.populateContactForm(userData.contacts || []);
      this.selectedUserId = userData.id;
      this.editMode = true;
      this.changeDetector.detectChanges();
    },
    error: (err: any) => {
      console.error('Failed to load user for edit:', err);
      Swal.fire('Error', 'Could not load user data.', 'error');
    }
  });
}


  // Hydrate banks into FormArray
  populateBankForm(banks: any[]): void {
    const bankFGs = banks.map((bank: any) => {
      const accountsArray = (bank.accounts || []).map((acc: any) =>
        this.fb.group({
          id: acc.id || '',
          accountHolderName: acc.accountHolderName || '',
          accountNumber: acc.accountNumber || '',
          accountType: acc.accountType || '',
          ifscCode: acc.ifscCode || '',
          bankName: acc.bankName || '',
          branchName: acc.branchName || '',
          bankAddress: acc.bankAddress || '',
          balance: acc.balance || '',

          deleted: acc.deleted || false
        })
      );

      return this.fb.group({
        id: bank.id || '',
        bankName: bank.bankName || '',
        bankType: bank.bankType || '',
        branchName: bank.branchName || '',
        bankAddress: bank.bankAddress || '',
        email: bank.email || '',
        phone: bank.phone || '',
        managerName: bank.managerName || '',
        establishedDate: bank.establishedDate
          ? this.formatToISO(bank.establishedDate)
          : '',
        deleted: bank.deleted || false,
        accounts: this.fb.array(accountsArray)
      });
    });

    this.dashboardForm.setControl('banks', this.fb.array(bankFGs));
    if (!this.editMode && bankFGs.length === 0) {
      (this.dashboardForm.get('banks') as FormArray).push(this.createBankGroup());
    }
  }

  // Hydrate contacts into FormArray
  populateContactForm(contacts: any[]): void {
    const contactFGs = contacts.map(contact =>
      this.fb.group({
        id: contact.id || '',
        name: contact.name || '',
        relation: contact.relation || '',
        mobile: contact.mobile || '',
        email: contact.email || '',
        address: contact.address || '',
        deleted: contact.deleted || false
      })
    );

    this.dashboardForm.setControl('contacts', this.fb.array(contactFGs));
    if (!this.editMode && contactFGs.length === 0) {
      (this.dashboardForm.get('contacts') as FormArray).push(this.createContactFormGroup());
    }
  }

  // Load datalist options for states & districts
  // loadDatalistOptions(countryId: number, stateName?: string, districtName?: string): void {
  //   if (!countryId) return;

  //   this.locationService.getStatesByCountry(countryId).pipe(
  //     tap(states => console.log('States loaded:', states)), // Debug
  //     switchMap(states => {
  //       this.states = states;

  //       // Normalize state name for comparison
  //       const normalizedStateName = stateName?.trim().toLowerCase();
  //       console.log("Normalized state name:", normalizedStateName);

  //       const stateExists = states.some(s => s.name.trim().toLowerCase() === normalizedStateName);
  //       console.log("State exists:", stateExists);

  //       if (!normalizedStateName || !stateExists) {
  //         this.dashboardForm.get('state')?.reset();
  //         this.dashboardForm.get('district')?.reset();
  //         this.dashboardForm.get('pincode')?.reset();
  //         return of([]); // No districts to load
  //       }

  //       // Pre-select state
  //       const selectedState = states.find(s => s.name.trim().toLowerCase() === normalizedStateName)!;
  //       this.dashboardForm.get('state')?.setValue(selectedState.name);

  //       // Fetch districts for selected state
  //       return this.locationService.getDistrictsByState(selectedState.id);
  //     }),
  //     tap(districts => console.log('Districts loaded:', districts)) // Debug
  //   ).subscribe(districts => {
  //     this.districts = districts;

  //     // Normalize district name for comparison
  //     const normalizedDistrictName = districtName?.trim().toLowerCase();
  //     const districtExists = districts.some(d => d.name.trim().toLowerCase() === normalizedDistrictName);

  //     if (normalizedDistrictName && districtExists) {
  //       const selectedDistrict = districts.find(d => d.name.trim().toLowerCase() === normalizedDistrictName)!;
  //       this.dashboardForm.get('district')?.setValue(selectedDistrict.name);

  //       // Load pincode for pre-selected district
  //       if (selectedDistrict.id) {
  //         this.locationService.getPincodeByDistrict(selectedDistrict.id).subscribe(pincode => {
  //           this.dashboardForm.get('pincode')?.setValue(pincode.code || '');
  //         });
  //       }
  //     } else {
  //       this.dashboardForm.get('district')?.reset();
  //       this.dashboardForm.get('pincode')?.reset();
  //     }
  //   });
  // }
  




  formatToISO(date: any): string {
    if (!date) return '';
    const d = new Date(date);
    return d.toISOString().split('T')[0]; // returns 'YYYY-MM-DD'
  }


  /** Open Preview Modal */
  openContactsPreview() {
    this.contactsPreviewData = this.contacts.controls.map(c => ({
      userId: this.dashboardForm.get('id')?.value || '',
      id: c.get('id')?.value || '',
      name: c.get('name')?.value || '',
      relation: c.get('relation')?.value || '',
      mobile: c.get('mobile')?.value || '',
      email: c.get('email')?.value || '',
      address: c.get('address')?.value || ''
    }));
    this.dialog.open(this.contactsPreviewTemplate, { width: '1100px' });
  }

  /** Open/Edit Excel pre-populated */
  // exportContactsToExcel() {
  //   const data = this.contacts.controls.map(c => ({
  //     userId: this.dashboardForm.get('id')?.value || '',
     
  //     name: c.get('name')?.value || '',
  //     relation: c.get('relation')?.value || '',
  //     mobile: c.get('mobile')?.value || '',
  //     email: c.get('email')?.value || '',
  //     address: c.get('address')?.value || ''
  //   }));

  //   const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(data, {
  //     header: ['userId', 'name', 'relation', 'mobile', 'email', 'address']
  //   });
  //   const wb: XLSX.WorkBook = XLSX.utils.book_new();
  //   XLSX.utils.book_append_sheet(wb, ws, 'Contacts');
  //   XLSX.writeFile(wb, 'Contacts.xlsx');
  // }

 async exportContactsStyled() {
  const data = this.contacts.controls.map(c => ({
    userId: this.dashboardForm.get('id')?.value || '',
    name: c.get('name')?.value || '',
    relation: c.get('relation')?.value || '',
    mobile: c.get('mobile')?.value || '',
    email: c.get('email')?.value || '',
    address: c.get('address')?.value || ''
  }));

  const workbook = new ExcelJS.Workbook();
  workbook.creator = 'YourApp';
  workbook.created = new Date();

  const sheet = workbook.addWorksheet('Contacts', {
    views: [{ state: 'frozen', ySplit: 1, xSplit: 1 }]
  });

  // ✅ Headers without `id`
  sheet.columns = [
    { header: 'userId',   key: 'userId',   width: 12 },
    { header: 'name',     key: 'name',     width: 25 },
    { header: 'relation', key: 'relation', width: 15 },
    { header: 'mobile',   key: 'mobile',   width: 18 },
    { header: 'email',    key: 'email',    width: 30 },
    { header: 'address',  key: 'address',  width: 40 }
  ];

  // 🎨 Style header row
  const headerRow = sheet.getRow(1);
  headerRow.height = 22;
  headerRow.eachCell(cell => {
    cell.font = { bold: true, color: { argb: 'FFFFFFFF' } };
    cell.alignment = { vertical: 'middle', horizontal: 'center', wrapText: true };
    cell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'FF1F4E78' } };
    cell.border = {
      top: { style: 'thin' }, left: { style: 'thin' },
      bottom: { style: 'thin' }, right: { style: 'thin' }
    };
  });

  // Add rows
  data.forEach(d => sheet.addRow(d));

  sheet.getColumn('mobile').numFmt = '@'; // mobile as text
  sheet.autoFilter = { from: 'A1', to: 'F1' };

  // Wrap address + zebra striping
  sheet.eachRow((row, rowNumber) => {
    if (rowNumber > 1) {
      row.getCell('address').alignment = { wrapText: true, vertical: 'top' };
      if (rowNumber % 2 === 0) {
        row.eachCell(cell => {
          cell.fill = {
            type: 'pattern',
            pattern: 'solid',
            fgColor: { argb: 'FFF2F2F2' }
          };
        });
      }
    }
  });

  const buf = await workbook.xlsx.writeBuffer();
  const blob = new Blob([buf], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
  saveAs(blob, 'Contacts_styled.xlsx');
}

  /** Upload Edited Excel */
  onFileUpload(event: any) {
  const file = event.target.files[0];
  if (!file) return;

  const reader = new FileReader();
  reader.onload = (e: any) => {
    const data = new Uint8Array(e.target.result);
    const workbook = XLSX.read(data, { type: 'array' });
    const sheet = workbook.Sheets[workbook.SheetNames[0]];
    const excelData: any[] = XLSX.utils.sheet_to_json(sheet);

    excelData.forEach(row => {
      const rowContactId = row.id?.toString(); // unique per contact
      const existingIndex = this.contacts.controls.findIndex(c =>
        c.get('id')?.value?.toString() === rowContactId
      );

      if (existingIndex >= 0) {
        // ✅ Update existing contact
        this.contacts.at(existingIndex).patchValue({
          name: row.name || '',
          relation: row.relation || '',
          mobile: row.mobile || '',
          email: row.email || '',
          address: row.address || ''
        });
      } else {
        // ✅ Add new contact
        this.contacts.push(this.fb.group({
          contactId: rowContactId || null,
          userId: row.userId || this.dashboardForm.get('id')?.value || '',
          name: row.name || '',
          relation: row.relation || '',
          mobile: row.mobile || '',
          email: row.email || '',
          address: row.address || '',
          deleted: false
        }));
      }
    });
  };

  reader.readAsArrayBuffer(file);
}
}

