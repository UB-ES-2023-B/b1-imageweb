import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse   } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private domain: string | undefined

  constructor(private http: HttpClient) {
    this.domain = environment.domain;
  }

  register(username: string, email: string, password: string): Observable<any> {
    const formData = {
      username: username,
      email: email,
      password: password
    };
    return this.http.post(this.domain + '/auth/register', formData,{ observe: 'response'});
  }

  login(username: string, password: string): Observable<any> {
    const formData = {
      username: username,
      password: password
    };
    return this.http.post(this.domain + '/auth/login', formData,{ observe: 'response'});
  }


}
