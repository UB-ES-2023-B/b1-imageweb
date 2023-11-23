import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
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

  setUserProfilePic(photo_file: File): Observable<string> {
    const formData = new FormData();
    formData.append('profilePhoto', photo_file);
    return this.http.post(this.domain + '/user/uploadPhotoProfile', formData,{ responseType: 'text' });
  }

  updateUser(username: string, updatedUser: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const options = { headers: headers };
    return this.http.put(this.domain + `/user/update/${username}`, updatedUser, options);
  }

  deleteUserProfilePhoto(): Observable<string> {
    return this.http.delete(this.domain + '/user/deleteProfilePhoto', { responseType: 'text' });
  }

  changeUserPassword(passwords: any): Observable<any> {
    return this.http.put(this.domain + '/user/resetPassword', passwords);
  }

  getSearchResults(query: string){
    return this.http.get(this.domain + `/search/getSearchResults/${query}`, {  observe: 'response' });
  }
}
