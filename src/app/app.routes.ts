import { Routes } from '@angular/router';

import { LoginComponent } from './auth/login/login';
import { SignupComponent } from './auth/signup/signup';
import { UserComponent } from './user/user';
import { SearchComponent } from './search/search';
import { NavbarComponent } from './core/navbar/navbar';
import { authGuard } from './auth-guard';
import { SupportComponent } from './support/support';
import path from 'path/win32';

export const routes: Routes = [

{
  path:'login' ,
  component:LoginComponent,
  loadChildren:()=>import('./auth/auth-module').then(m=>m.AuthModule)
},
{
  path:'signup',
  component:SignupComponent,
  loadChildren:()=>import('./auth/auth-module').then(m=>m.AuthModule)

},
{
  path:'user',
  component:UserComponent,
  loadChildren:()=>import('./user/user-module').then(m=>m.UserModule),
  canActivate:[authGuard]
},
{
  path:'user/:id',
   component:UserComponent,
  loadChildren:()=>import('./user/user-module').then(m=>m.UserModule),
    canActivate:[authGuard]

},

{
  path:'search',
  loadChildren:()=>import('./search/search-module').then(m=>m.SearchModule),
    canActivate:[authGuard]
},
{
  path:'navbar',
  component:NavbarComponent,
  loadChildren:()=>import('./core/core-module').then(m=>m.CoreModule)

},
{
path:'support',
loadComponent:()=>import('./support/support').then(m=>m.SupportComponent),
canActivate:[authGuard]
},
{
path:'home',
loadComponent:()=>import('./home/home').then(m=>m.HomeComponent)
},
{
  path:'about',
  loadComponent:()=>import('./about/about').then(m=>m.AboutComponent),
  canActivate:[authGuard]
 
},
{
  path: 'payment',
  loadComponent: () => import('./user/payment/payment').then(m => m.PaymentComponent),
  canActivate: [authGuard]
},
{
  path: 'transaction',
  loadComponent: () => import('./user/transaction/transaction').then(m => m.TransactionComponent),
  canActivate: [authGuard]
},
{
  path: '**', redirectTo: '/home'
},

];
