import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getUser(username: string): Observable<any> {
    return this.http.get(`/api/user/getByUserName/${username}`, {  observe: 'response' });
  }

  setUserProfilePic(profilePhoto: string): Observable<any> {
    const requestBody = { profilePhoto: profilePhoto };

    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const options = { headers: headers };

    return this.http.post('/api/user/uploadPhotoProfile', requestBody, options);
  }

  updateUser(username: string, updatedUser: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const options = { headers: headers };

    return this.http.put(`/api/user/update/${username}`, updatedUser, options);
  }
}
