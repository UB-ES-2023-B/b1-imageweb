import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class FollowersService {
  private domain: string |undefined


  constructor(private http: HttpClient) {
    this.domain = environment.domain;
   }

  getFollowers(username: string): Observable<any> {
    return this.http.get(this.domain + `/user/getFollowers/${username}`, {  observe: 'response' });
  }
  getFollowing(username: string): Observable<any> {
    return this.http.get(this.domain + `/user/getFollowing/${username}`, {  observe: 'response' });
  }

  followUser(username: string): Observable<any> {
    return this.http.post(this.domain + `/user/follow/${username}`, { observe: 'response' });
  }

  unfollowUser(username: string): Observable<any> {
    return this.http.post(this.domain + `/user/unfollow/${username}`, { observe: 'response' });
  }


}
