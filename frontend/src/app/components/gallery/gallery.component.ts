import { Component } from '@angular/core';
import { GalleryService } from 'src/app/services/gallery.service';
import { GlobalDataService } from '../../services/global-data.service';


@Component({
  selector: 'app-gallery',
  templateUrl: './gallery.component.html',
  styleUrls: ['./gallery.component.css']
})
export class GalleryComponent {

  images:any[]=[];

  constructor(private galleryService:GalleryService){

  }
  getGallery():void{
    this.galleryService.getGalleryPhotos().subscribe(
      (response)=>{
        if (response.body && Array.isArray(response.body)) {
          response.body.forEach((element: any) => {
            if (element.data) {
              this.images.push({"data":element.data, "type":element.photoName.slice(-3),
            "imageSrc":`data:image/${element.photoName.slice(-3)};base64,${element.data}`});
            }
          });
        }
      },
      (error)=>{
        console.log('error al obtener all gallery', error)
      }
    )
  }
  ngOnInit(): void {
    this.getGallery();

  }




}
