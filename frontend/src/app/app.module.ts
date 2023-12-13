import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { CommonModule } from '@angular/common';
import { NgxBootstrapIconsModule } from 'ngx-bootstrap-icons';
import { allIcons } from 'ngx-bootstrap-icons';
import { AuthGuard } from './guards/auth.guard';
import { ToastrModule } from 'ngx-toastr';
import { ImageCropperModule } from 'ngx-image-cropper';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LightboxModule } from 'ngx-lightbox';
import { FileSaverModule } from 'ngx-filesaver';

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
import { UploadPhotoComponent } from './components/upload-photo/upload-photo.component';
import { EditProfileComponent } from './components/edit-profile/edit-profile.component';
import { ChangePasswordComponent } from './components/change-password/change-password.component';
import { ModalComponent } from './components/modal/modal.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { EditGalleryComponent } from './components/edit-gallery/edit-gallery.component';
import { AlbumsComponent } from './components/albums/albums.component';
import { UploadPhotosComponent } from './components/upload-photos/upload-photos.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { EditAlbumsComponent } from './components/edit-albums/edit-albums.component';
import { AlbumViewComponent } from './components/album-view/album-view.component';
import {AlbumViewEditModeComponent} from "./components/album-view-edit-mode/album-view-edit-mode.component";

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
    GalleryComponent,
    UploadPhotoComponent,
    EditProfileComponent,
    ChangePasswordComponent,
    ModalComponent,
    EditGalleryComponent,
    AlbumsComponent,
    UploadPhotosComponent,
    UserProfileComponent,
    EditAlbumsComponent,
    UploadPhotosComponent,
    AlbumViewComponent,
    AlbumViewEditModeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    CommonModule,
    NgxBootstrapIconsModule.pick(allIcons),
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    ImageCropperModule,
    LightboxModule,
    FileSaverModule,
    NgbModule
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
