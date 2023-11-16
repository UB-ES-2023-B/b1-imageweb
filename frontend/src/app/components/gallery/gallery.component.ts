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
  isEditMode = false;
  selectedImageIds: number[] = [];



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



  cancelEditMode() {
    this.isEditMode = false;
    this.selectedImageIds = [];
  }


  deleteSelectedImages() {
    // Agregar la llamada del back
    console.log('Eliminar imágenes seleccionadas:', this.selectedImageIds);
    this.isEditMode = false;
    this.selectedImageIds = [];
  }
  toggleEditMode(id: number) {
    if (this.isEditMode) {
      // Si ya estamos en modo de edición, significa que se hizo clic en el checkbox
      const index = this.selectedImageIds.indexOf(id);
      if (index === -1) {
        // Si el ID no está en la lista, lo añadimos
        this.selectedImageIds.push(id);
      } else {
        // Si el ID ya está en la lista, lo eliminamos
        this.selectedImageIds.splice(index, 1);
      }
    } else {
      // Si no estamos en modo de edición, activamos el modo y añadimos la imagen actual
      this.isEditMode = true;
      this.selectedImageIds.push(id);
    }
    console.log('Lista de imágenes seleccionadas:', this.selectedImageIds);
  }



}
