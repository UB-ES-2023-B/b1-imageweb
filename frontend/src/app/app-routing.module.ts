import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MainPageComponent} from './components/main-page/main-page.component';
import {LoginFormComponent} from "./components/login-page/login-page.component";
import {RegisterFormComponent} from "./components/register-page/register-page.component";
import { HomePageComponent } from './components/home-page/home-page.component';
import { ProfilePageComponent } from './components/profile-page/profile-page.component';
import { AuthGuard  } from './guards/auth.guard';
import {EditProfileComponent} from "./components/edit-profile/edit-profile.component";
import { EditGalleryComponent } from './components/edit-gallery/edit-gallery.component';
import {AlbumViewComponent} from "./components/album-view/album-view.component";
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
    component: HomePageComponent,
    canActivate: [AuthGuard ]
  },
  {
    path: 'register',
    component: RegisterFormComponent
  },
  {
    path: 'profile',
    component: ProfilePageComponent,
    canActivate: [AuthGuard ]
  },
  {
    path: 'profile/edit',
    component: EditProfileComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'profile/editGallery',
    component: EditGalleryComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'profile/album/:id',
    component: AlbumViewComponent,
    canActivate: [AuthGuard]
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
