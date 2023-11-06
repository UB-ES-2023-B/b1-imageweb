import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getUser(username: string): Observable<any> {
    return this.http.get(`/api/user/getByUserName/${username}`, {  observe: 'response' });
  }

  setUserProfilePic(photo_file: File): Observable<string> {
    const formData = new FormData();
    formData.append('profilePhoto', photo_file);
    return this.http.post('/api/user/uploadPhotoProfile', formData,{ responseType: 'text' });
  }

  updateUser(username: string, updatedUser: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const options = { headers: headers };

    return this.http.put(`/api/user/update/${username}`, updatedUser, options);
  }

  deleteUserProfilePhoto(): Observable<string> {
    return this.http.delete('/api/user/deleteProfilePhoto', { responseType: 'text' });
  }
}
