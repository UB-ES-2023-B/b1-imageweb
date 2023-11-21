import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import {environment} from "../../environments/environment";


@Injectable({
  providedIn: 'root'
})
export class GalleryService {
  private domain: string |undefined
  private images: any[] = [];
  private imagesSubject: BehaviorSubject<any[]> = new BehaviorSubject(this.images);

  constructor(private http: HttpClient) {
    this.domain = environment.domain;
  }

 // Cambiar por la de byUserName
  getGalleryPhotos(): Observable<any> {
    return this.http.get(this.domain + `/gallery/getAll`, {  observe: 'response' });
  }

  uploadImage(idGallery: string, photo_file: File): Observable<any> {
    const formData = new FormData();

    formData.append('photo', photo_file);
    return this.http.post(this.domain + `/gallery/uploadPhotoGalery/${idGallery}`, formData,{  observe: 'response'});
  }
  getGalleryUser(userName:string): Observable<any> {
    return this.http.get(this.domain + `/gallery/viewGalleryFromUser/${userName}`, {  observe: 'response' });
  }

  deletePhotoGallery(id: number[]): Observable<any> {
    const formData = {
      photoIds: id
    };
    return this.http.delete(this.domain + '/gallery/deletephotos', {
      body: formData,
      observe: 'response'
    });
  }

  setImages(images: any[]): void {
    this.images = images;
    this.imagesSubject.next(this.images);
  }

  getImages(): any[] {
    return this.images;
  }

  addImage(image: any, id:number, name:string, description:string): void {

    this.images.unshift({"src":image, "id": id, "name":name, "description": description});
    this.imagesSubject.next(this.images);
  }

  getImagesObservable(): Observable<any[]> {
    return this.imagesSubject.asObservable();
  }



}
