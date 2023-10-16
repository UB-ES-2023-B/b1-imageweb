import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class GlobalDataService {
  private usernameSubject = new BehaviorSubject<string>(sessionStorage.getItem('username') || '');
  username$ = this.usernameSubject.asObservable()
  public email: string='';

  setUsername(newUsername: string) {
    sessionStorage.setItem('username', newUsername);
    this.usernameSubject.next(newUsername);
  }

  getUsername(): string {
    return this.usernameSubject.getValue();
  }

  clearSession() {
    sessionStorage.removeItem('username');
    this.usernameSubject.next('');
  }

}
