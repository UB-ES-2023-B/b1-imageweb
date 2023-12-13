import { Injectable } from '@angular/core';
import { environment } from "../../environments/environment";
import { HttpClient } from '@angular/common/http';
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class MuroService {
  private domain: string | undefined

  constructor(private http: HttpClient) {
    this.domain = environment.domain;
  }

  getMuro(): Observable<any>  {
    return this.http.get(this.domain + `/getMuro`, { observe: 'response' });
  }

}
