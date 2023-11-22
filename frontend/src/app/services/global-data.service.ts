import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {key} from "ngx-bootstrap-icons";

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
  private descriptionSubject = new BehaviorSubject<string>(sessionStorage.getItem('description') || '');
  private profilePictureSubject = new BehaviorSubject<ProfilePictureInfo>({
    file: null,
    previousUrl: '',
  });

  token$ = this.tokenSubject.asObservable()
  username$ = this.usernameSubject.asObservable()
  email$ = this.emailSubject.asObservable()
  profilePicture$ = this.profilePictureSubject.asObservable()

  setToken(newToken: string) {
    sessionStorage.setItem('token', newToken);
    this.tokenSubject.next(newToken);
  }

  getToken(): string { return this.tokenSubject.getValue(); }

  setUsername(newUsername: string) {
    sessionStorage.setItem('username', newUsername);
    this.usernameSubject.next(newUsername);
  }

  getUsername(): string { return this.usernameSubject.getValue(); }

  setEmail(newEmail: string) {
    sessionStorage.setItem('email', newEmail);
    this.emailSubject.next(newEmail);
  }

  getEmail(): string { return this.emailSubject.getValue(); }

  setDescription(newDescription: string) {
    sessionStorage.setItem('description', newDescription);
    this.descriptionSubject.next(newDescription);
  }

  getDescription(): string { return this.descriptionSubject.getValue(); }

  setGalleryId(galleryId: string) {
    sessionStorage.setItem('galleryId', galleryId);
    this.galleryIdSubject.next(galleryId);
  }

  getGalleryId(): string { return this.galleryIdSubject.getValue(); }

  setProfilePicture(profilePicture: File | null, previousUrl: string) {
    if (profilePicture) {
      sessionStorage.setItem('profilePicture', 'true'); // Almacena una marca para indicar que hay una imagen de perfil
      sessionStorage.setItem('profilePictureFile', JSON.stringify(profilePicture));
      sessionStorage.setItem('profilePicturePreviousUrl', previousUrl);
    } else {
      sessionStorage.removeItem('profilePicture'); // Elimina la marca si no hay imagen de perfil
      sessionStorage.removeItem('profilePictureFile');
      sessionStorage.removeItem('profilePicturePreviousUrl');
    }
    this.profilePictureSubject.next({ file: profilePicture, previousUrl });
  }

  getProfilePicture(): ProfilePictureInfo { return this.profilePictureSubject.getValue(); }
  //getProfilePicture(): Observable<ProfilePictureInfo> { return this.profilePicture$; }


  get activeItem$() { return this._activeItem.asObservable(); }

  // Proporciona un método para actualizar el valor de activeItem
  setActiveItem(newItem: string) { this._activeItem.next(newItem); }

  clearSession() {
    sessionStorage.removeItem('username');
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('email');
    sessionStorage.removeItem('galleryId');
    sessionStorage.removeItem('profilePicture');
    sessionStorage.removeItem('profilePictureFile');
    sessionStorage.removeItem('profilePicturePreviousUrl');
    sessionStorage.removeItem('description');

    this.usernameSubject.next('');
    this.emailSubject.next('');
    this.descriptionSubject.next('')
    this.profilePictureSubject.next({ file: null, previousUrl: '../assets/images/perfil.jpg' });
  }
}
