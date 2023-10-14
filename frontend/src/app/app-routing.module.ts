import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MainPageComponent} from './components/main-page/main-page.component';
import {LoginFormComponent} from "./components/login-page/login-page.component";
import {RegisterFormComponent} from "./components/register-page/register-page.component";

const routes: Routes = [
   //Initial route
   {
    path:'',
    redirectTo:'/loadingPage',
    pathMatch:'full'
  },
  {
    path:'loadingPage',
    component:MainPageComponent
  },
  {
    path: 'loginUser',
    component: LoginFormComponent
  },
  {
    path: 'authenticateRegister',
    component: RegisterFormComponent
  },
  {
    path: '**',
    redirectTo: '/loadingPage'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
