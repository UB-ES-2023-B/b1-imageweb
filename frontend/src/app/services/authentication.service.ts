import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse   } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private http: HttpClient) { }

  register(username: string, email: string, password: string): Observable<any> {
    const formData = {
      username: username,
      email: email,
      password: password
    };
    return this.http.post('/api/auth/register', formData,{ observe: 'response'});
  }

  login(username: string, password: string): Observable<any> {
    const formData = {
      username: username,
      password: password
    };
    return this.http.post('/api/auth/login', formData,{ observe: 'response'});
  }


}
