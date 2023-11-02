import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class GalleryService {
  private images: any[] = [];
  private imagesSubject: BehaviorSubject<any[]> = new BehaviorSubject(this.images);
  constructor(private http: HttpClient) { }

 // Cambiar por la de byUserName
  getGalleryPhotos(): Observable<any> {
    return this.http.get(`/api/gallery/getAll`, {  observe: 'response' });
  }

  uploadImage(idGallery: string, photo_file: File):  Observable<string> {
    const formData = new FormData();

    formData.append('photo', photo_file);
    return this.http.post(`/api/gallery/uploadPhotoGalery/${idGallery}`, formData,{  responseType: 'text'});
  }
  getGalleryUser(userName:string): Observable<any> {
    return this.http.get(`/api/gallery/viewGalleryFromUser/${userName}`, {  observe: 'response' });
  }


  setImages(images: any[]): void {
    this.images = images;
    this.imagesSubject.next(this.images);
  }

  getImages(): any[] {
    return this.images;
  }

  addImage(image: any): void {
    this.images.push({"imageSrc":image});
    this.imagesSubject.next(this.images);
  }

  getImagesObservable(): Observable<any[]> {
    return this.imagesSubject.asObservable();
  }

}
