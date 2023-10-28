import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { CommonModule } from '@angular/common';

import { AuthGuard } from './guards/auth.guard';

import { AuthInterceptor } from './interceptors/auth.interceptor';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { FooterComponent } from './components/footer/footer.component';
import { MainPageComponent } from './components/main-page/main-page.component';
import {LoginFormComponent} from "./components/login-page/login-page.component";
import {RegisterFormComponent} from "./components/register-page/register-page.component";
import { HomePageComponent } from './components/home-page/home-page.component';
import { ProfilePageComponent } from './components/profile-page/profile-page.component';
import { SecondaryNavbarComponent } from './components/secondary-navbar/secondary-navbar.component';
import { GalleryComponent } from './components/gallery/gallery.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    MainPageComponent,
    LoginFormComponent,
    RegisterFormComponent,
    RegisterFormComponent,
    HomePageComponent,
    ProfilePageComponent,
    SecondaryNavbarComponent,
    GalleryComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    CommonModule
  ],
  providers: [

    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }

  ],
    bootstrap: [AppComponent]
  })
  export class AppModule { }
