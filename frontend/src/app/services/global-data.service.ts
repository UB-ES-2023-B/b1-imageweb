import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class GlobalDataService {
  private usernameSubject = new BehaviorSubject<string>(sessionStorage.getItem('username') || '');
  private tokenSubject = new BehaviorSubject<string>(sessionStorage.getItem('token') || '');
  username$ = this.usernameSubject.asObservable()
  token$ = this.tokenSubject.asObservable()
  public email: string='';

  setToken(newToken: string) {
    sessionStorage.setItem('token', newToken);
    this.usernameSubject.next(newToken);
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

  clearSession() {
    sessionStorage.removeItem('username');
    sessionStorage.removeItem('token');
    this.usernameSubject.next('');
  }

}
