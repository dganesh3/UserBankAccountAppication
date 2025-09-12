import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SearchRoutingModule } from './search-routing-module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from 'primeng/api';
import { SearchComponent } from './search';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MessageService, ConfirmationService } from 'primeng/api';
@NgModule({
  // declarations: [SearchComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SharedModule,
    SearchRoutingModule,
    SearchComponent,
     ToastModule,
    ConfirmDialogModule
  ],
  exports:[
   SearchComponent
  ],
 providers: [
    MessageService,
    ConfirmationService
  ]
})
export class SearchModule { }
