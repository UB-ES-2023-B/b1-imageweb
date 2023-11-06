import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

interface ProfilePictureInfo {
  file: File | null;
  previousUrl: string;
}


@Injectable({
  providedIn: 'root'
})
export class GlobalDataService {

  private _activeItem: BehaviorSubject<string> = new BehaviorSubject<string>('gallery');
  private usernameSubject = new BehaviorSubject<string>(sessionStorage.getItem('username') || '');
  private tokenSubject = new BehaviorSubject<string>(sessionStorage.getItem('token') || '');
  private emailSubject = new BehaviorSubject<string>(sessionStorage.getItem('email') || '');
  private galleryIdSubject = new BehaviorSubject<string>(sessionStorage.getItem('galleryId') || '');
  private profilePictureSubject: BehaviorSubject<ProfilePictureInfo> = new BehaviorSubject<ProfilePictureInfo>({
    file: null,
    previousUrl: '',
  });


  username$ = this.usernameSubject.asObservable()
  token$ = this.tokenSubject.asObservable()
  email$ = this.emailSubject.asObservable()

  setToken(newToken: string) {
    sessionStorage.setItem('token', newToken);
    this.tokenSubject.next(newToken);
  }

  getToken(): string {
    return this.tokenSubject.getValue();
  }
  setUsername(newUsername: string) {
    sessionStorage.setItem('username', newUsername);
    this.usernameSubject.next(newUsername);
  }

  getUsername(): string {
    return this.usernameSubject.getValue();
  }

  setEmail(newEmail: string) {
    sessionStorage.setItem('email', newEmail);
    this.emailSubject.next(newEmail);
  }

  getEmail(): string {
    return this.emailSubject.getValue();
  }

  clearSession() {
    sessionStorage.removeItem('username');
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('email');
    sessionStorage.removeItem('galleryId');
    sessionStorage.removeItem('profilePicture')
    this.usernameSubject.next('');
    this.emailSubject.next('');
  }

  setGalleryId(galleryId: string) {
    sessionStorage.setItem('galleryId', galleryId);
    this.galleryIdSubject.next(galleryId);
  }

  getGalleryId(): string {
    return this.galleryIdSubject.getValue();
  }

  setProfilePicture(profilePicture: File | null, previousUrl: string) {
    if (profilePicture) sessionStorage.setItem('profilePicture', 'true'); //Almacenar un marcador para indicar que hay una imagen de perfil
    else sessionStorage.removeItem('profilePicture'); // Elimina el marcador si no hay imagen de perfil

    this.profilePictureSubject.next({ file: profilePicture, previousUrl });
  }

  getProfilePicture(): ProfilePictureInfo {
    return this.profilePictureSubject.getValue();
  }

  get activeItem$() {
    return this._activeItem.asObservable();
  }

  // Proporciona un método para actualizar el valor de activeItem
  setActiveItem(newItem: string) {
    this._activeItem.next(newItem);
  }

}
