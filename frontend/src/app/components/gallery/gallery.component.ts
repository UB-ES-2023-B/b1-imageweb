import { Component } from '@angular/core';
import { GalleryService } from 'src/app/services/gallery.service';
import { GlobalDataService } from 'src/app/services/global-data.service';

@Component({
  selector: 'app-gallery',
  templateUrl: './gallery.component.html',
  styleUrls: ['./gallery.component.css']
})
export class GalleryComponent {
  loading:boolean=true;
  images:any[]=[];

  constructor(private galleryService:GalleryService, private globalDataService:GlobalDataService){
  }
  getGallery():void{
    this.galleryService.getGalleryUser(this.globalDataService.getUsername()).subscribe(
      (response)=>{
        if (response.body && Array.isArray(response.body)) {
          response.body.forEach((element: any) => {
            if (element.data) {
              this.images.push({"data":element.data, "type":element.photoName.slice(-3),
            "imageSrc":`data:image/${element.photoName.slice(-3)};base64,${element.data}`});
            }
          });
        }
        this.loading=false;
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
