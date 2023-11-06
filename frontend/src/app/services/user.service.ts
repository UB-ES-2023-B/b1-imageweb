import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private domain: string |undefined

  constructor(private http: HttpClient) {
    this.domain = environment.domain;
  }

  getUser(username: string): Observable<any> {
    return this.http.get(this.domain + `/user/getByUserName/${username}`, {  observe: 'response' });
  }
}
