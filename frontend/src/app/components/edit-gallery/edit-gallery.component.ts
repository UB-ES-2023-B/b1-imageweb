import { Component } from '@angular/core';
import { GalleryService } from 'src/app/services/gallery.service';
import { GlobalDataService } from 'src/app/services/global-data.service';
import { Subscription } from 'rxjs';
import { Lightbox } from 'ngx-lightbox';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
@Component({
  selector: 'app-edit-gallery',
  templateUrl: './edit-gallery.component.html',
  styleUrls: ['./edit-gallery.component.css']
})
export class EditGalleryComponent {
  loading:boolean=true;
  images:any[]=[];
  showUploadHint: boolean = true;
  isEditMode = false;
  selectedImageIds: number[] = [];
  editImageId:number=-1;
  editImageOriginalValues:any= {};

  modalResponse: string = '';

  classModal:string='text-center mb-4'


  private imagesSubscription: Subscription = new Subscription();


  constructor(private galleryService:GalleryService, private router:Router, private globalDataService:GlobalDataService, private _lightbox: Lightbox, private toastr: ToastrService){
  }
  goGallery(){
    this.router.navigate(['/profile/']);
  }

  changeIsModal(){
    this.classModal='text-center mb-4 hideText'
  }

  handleModalClosed(response: string) {


    if(response=='Save click'){
      this.loading=true;
      this.deleteSelectedImages()
    }else{
      this.classModal='text-center mb-4'
    }

    this.modalResponse = response;

  }

  startEditInfo(id:number, name:string, description:string){
    this.editImageId=id;
    this.editImageOriginalValues={name:name,description:description}
  }
  cancelEditInfo(id:number){
    let index = this.images.findIndex(imagen => imagen.id === id);

    if (index !== -1) {
      this.images[index].name = this.editImageOriginalValues.name;
      this.images[index].description = this.editImageOriginalValues.description;

    } else {
      console.log('Error en cancelar');
    }
    this.editImageId=-1;
  }
  saveEditInfo(id:number, name:string, description:string){
    this.galleryService.editInfoPhoto(id,name, description).subscribe(
      (response)=>{
        this.toastr.success('Se ha editado correctamente');
        this.editImageId=-1;

      },
      (error)=>{
        console.log('error editar campos de fotos', error)
        this.toastr.error('No se ha editado correctamente','Error');
        let index = this.images.findIndex(imagen => imagen.id === id);

        if (index !== -1) {
          this.images[index].name = this.editImageOriginalValues.name;
          this.images[index].description = this.editImageOriginalValues.description;

        } else {
          console.log('Error en cancelar');
        }
        this.editImageId=-1;

      }
    )
    //Hacer la llamada al back
    //this.editImageId=-1;
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
             "id": element.photoId, "name":element.photoName, "description": element.photoDescription});
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

    this.galleryService.deletePhotoGallery( this.selectedImageIds).subscribe(
      (response)=>{
        console.log('Se ha borrado bien', response)
        this.getGallery()
      },
      (error)=>{
        console.log('error al eliminar', error)
        this.toastr.error('No se ha eliminado correctamente','Error');

      }
    )

    this.isEditMode = false;
    this.classModal='text-center mb-4 '
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
    // console.log('Lista de imágenes seleccionadas:', this.selectedImageIds);
  }


}
