import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GalleryService {

  constructor(private http: HttpClient) { }

 // Cambiar por la de byUserName
  getGalleryPhotos(): Observable<any> {
    return this.http.get(`/api/gallery/getAll`, {  observe: 'response' });
  }
}
