import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MainPageComponent} from './components/main-page/main-page.component';
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
    path: '**',
    redirectTo: '/loadingPage'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
