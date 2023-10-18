import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MainPageComponent} from './components/main-page/main-page.component';
import {LoginFormComponent} from "./components/login-page/login-page.component";
import {RegisterFormComponent} from "./components/register-page/register-page.component";
import { HomePageComponent } from './components/home-page/home-page.component';
import { ProfilePageComponent } from './components/profile-page/profile-page.component';

const routes: Routes = [
   //Initial route
   {
    path:'',
    redirectTo:'',
    pathMatch:'full'
  },
  {
    path:'',
    component:MainPageComponent
  },
  {
    path: 'login',
    component: LoginFormComponent
  },
  {
    path: 'home',
    component: HomePageComponent
  },
  {
    path: 'register',
    component: RegisterFormComponent
  },
  {
    path: 'profile',
    component: ProfilePageComponent
  },
  {
    path: '**',
    redirectTo: ''
  }
];

// @ts-ignore
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }
