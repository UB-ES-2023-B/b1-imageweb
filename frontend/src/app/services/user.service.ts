import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GlobalDataService } from './global-data.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient,
              private globalDataService: GlobalDataService) { }

  getUser(username: string): Observable<any> {
    return this.http.get(`/api/user/getByUserName/${username}`, {  observe: 'response' });
  }
}
