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

  //Aún no esta terminado
  getAlbumsForUser(): Observable<any> {
    return this.http.get(this.domain + `/getAlbums`, {  observe: 'response' });
  }

  addPhotosToAlbum(){
    // falta endpoint
  }

  setAlbums(albums: any[]): void {
    this.albums = albums;
    this.albumsSubject.next(this.albums);
  }

  getAlbums(): any[] {
    return this.albums;
  }

  addAlbums(imageFirst: any, id:number, name:string, description:string): void {

    this.albums.unshift({"src":imageFirst, "id": id, "name":name, "description": description});
    this.albumsSubject.next(this.albums);
  }

  getAlbumsObservable(): Observable<any[]> {
    return this.albumsSubject.asObservable();
  }




}
