import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class GlobalDataService {
  private usernameSubject = new BehaviorSubject<string>(sessionStorage.getItem('username') || '');
  private tokenSubject = new BehaviorSubject<string>(sessionStorage.getItem('token') || '');
  private emailSubject = new BehaviorSubject<string>(sessionStorage.getItem('email') || '');

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
    this.usernameSubject.next('');
    this.emailSubject.next('');
  }

}
