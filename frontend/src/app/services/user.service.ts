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
    const token = this.globalDataService.getToken();

    // Crea un objeto HttpHeaders con el encabezado de autorización
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    // Realiza la solicitud HTTP con los encabezados
    return this.http.get(`/api/user/getByUserName/${username}`, { headers, observe: 'response' });
  }
}
