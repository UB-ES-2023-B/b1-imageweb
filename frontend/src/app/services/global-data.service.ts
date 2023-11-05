import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class GlobalDataService {

  private _activeItem: BehaviorSubject<string> = new BehaviorSubject<string>('info');
  private usernameSubject = new BehaviorSubject<string>(sessionStorage.getItem('username') || '');
  private tokenSubject = new BehaviorSubject<string>(sessionStorage.getItem('token') || '');
  private emailSubject = new BehaviorSubject<string>(sessionStorage.getItem('email') || '');
  private galleryIdSubject = new BehaviorSubject<string>(sessionStorage.getItem('galleryId') || '');
  private profilePictureSubject: BehaviorSubject<any> = new BehaviorSubject<any>(sessionStorage.getItem('profilePicture') || '');

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

  setProfilePicture(profilePicture: any) {
    sessionStorage.setItem('profilePicture', profilePicture);
    this.profilePictureSubject.next(profilePicture);
  }

  getProfilePicture(): any {
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
