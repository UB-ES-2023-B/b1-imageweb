import { Component } from '@angular/core';
import { GalleryService } from 'src/app/services/gallery.service';
import { GlobalDataService } from 'src/app/services/global-data.service';
import { Subscription } from 'rxjs';
import { Lightbox } from 'ngx-lightbox';

@Component({
  selector: 'app-gallery',
  templateUrl: './gallery.component.html',
  styleUrls: ['./gallery.component.css']
})
export class GalleryComponent {
  loading:boolean=true;
  images:any[]=[];
  showUploadHint: boolean = true;
  showIcon = false;
  isEditMode = false;


  private imagesSubscription: Subscription = new Subscription();


  constructor(private galleryService:GalleryService, private globalDataService:GlobalDataService, private _lightbox: Lightbox){
  }
  getGallery():void{
    this.images=[]
    this.galleryService.getGalleryUser(this.globalDataService.getUsername()).subscribe(
      (response)=>{
        if (response.body && Array.isArray(response.body)) {
          response.body.forEach((element: any) => {
            if (element.data) {
              this.images.unshift({
            "src":`data:image/${element.photoExtensio};base64,${element.data}`,
             "id": element.photoId, "name":element.photoName, "description": 'Cambiar descripción'});
            }
          });
        }
        this.galleryService.setImages(this.images);
        this.loading=false;

      },
      (error)=>{
        console.log('error al obtener all gallery', error)
      }
    )
  }
  ngOnInit(): void {
    this.imagesSubscription = this.galleryService.getImagesObservable().subscribe((images) => {
      this.images = images;
    });
    this.getGallery();



  }

  open(index: number): void {
    // open lightbox
    this._lightbox.open(this.images, index);
  }

  close(): void {
    // close lightbox programmatically
    this._lightbox.close();
  }
  ngOnDestroy(): void {
    this.imagesSubscription.unsubscribe();
  }

  toggleEditMode() {
    this.isEditMode = !this.isEditMode;

    // Si estamos activando el modo de edición, seleccionamos automáticamente la imagen actual
    if (this.isEditMode) {
      const firstSelectedImage = this.images.find(image => image.isSelected);
      if (!firstSelectedImage) {
        // Si no hay ninguna imagen seleccionada, seleccionamos la primera
        this.images[0].isSelected = true;
      }
    }
  }


}
