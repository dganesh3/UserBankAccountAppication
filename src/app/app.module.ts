import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';

import { App } from './app';
import { CoreModule } from './core/core-module';
import { SharedModule } from './shared/shared-module';

import { UserModule } from './user/user-module';
import { SearchModule } from './search/search-module';
import { SupportComponent } from './support/support';
import { HomeComponent } from './home/home';
import { NavbarComponent } from './core/navbar/navbar';
import { AboutComponent } from './about/about';
import { CommonModule } from '@angular/common';
import { AuthService } from './authservice';


@NgModule({
  
providers: [
  {
    provide: APP_INITIALIZER,
    useFactory: (authService: AuthService) => () => authService.restoreAuth(),
    deps: [AuthService],
    multi: true
  },
 
],

  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    HttpClientModule,
    RouterModule,
    CoreModule,
    SharedModule,
    
    UserModule,
    SearchModule,
    SupportComponent,
    HomeComponent,
    AboutComponent,
    CommonModule,
 

  ],

})
export class AppModule {}
