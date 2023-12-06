import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AlbumsService {
  private domain: string |undefined
  private albums: any[] = [];
  private albumsSubject: BehaviorSubject<any[]> = new BehaviorSubject(this.albums);

  constructor(private http: HttpClient) {
    this.domain = environment.domain;
  }

  createAlbum(name: string, description: string, defaultImage: File): Observable<any> {
    const formData = new FormData();
    const data = { name: name, description: description };
    formData.append('coverPhoto', defaultImage);
    formData.append('album', JSON.stringify(data));

    return this.http.post(this.domain + `/newAlbum`, formData, { observe: 'response' });
  }

  updateInfoAlbum(id:number, name:string, description:string): Observable<any> {
    const data = { name: name, description: description };
    return this.http.put(this.domain + `/album/updateInfo/${id}`, data, { observe: 'response', responseType: 'text' });
  }

  deleteAlbums(ids:number[]): Observable<any> {
    const formData = {
      albumIds: ids
    };
    return this.http.delete(this.domain + '/album/delete', {
      body: formData,
      observe: 'response'
    });
  }

  //Aún no esta terminado
  getAlbumsForUser(username: string): Observable<any> {
    return this.http.get(this.domain + `/getAlbums/${username}`, {  observe: 'response' });
  }

  addPhotosToAlbum(idAlbum:number, photos:File[]){

    const formData = new FormData();

    photos.forEach((photo, index) => {
      formData.append(`photos`, photo);
    });

    return this.http.post(this.domain + `/addPhotosAlbum/${idAlbum}`, formData, { observe: 'response' });
  }

  setAlbums(albums: any[]): void {
    this.albums = albums;
    this.albumsSubject.next(this.albums);
  }

  getAlbums(): any[] {
    return this.albums;
  }

  addAlbums(imageFirst: any, id:number, name:string, description:string, len:number): void {

    this.albums.unshift({"src":imageFirst, "id": id, "name":name, "description": description, "photoLength":len});
    this.albumsSubject.next(this.albums);
  }

  modifylen(id:number, len:number): void {
    const index = this.albums.findIndex((item) => item.id === id);
    if (index !== -1) {
        // Si se encuentra el elemento, actualiza el campo photoLength
        this.albums[index].photoLength = len;
    }
    this.albumsSubject.next(this.albums);
  }

  modifyCoverPhoto(id:number, imageFirst: any): void {
    const index = this.albums.findIndex((item) => item.id === id);
    if (index !== -1) {
        // Si se encuentra el elemento, actualiza el campo photoLength
        this.albums[index].src = imageFirst;
    }
    this.albumsSubject.next(this.albums);
  }


  getAlbumsObservable(): Observable<any[]> {
    return this.albumsSubject.asObservable();
  }




}
