import { Component } from '@angular/core';
import { AlbumsService } from '../../services/albums.service';
import { GlobalDataService } from 'src/app/services/global-data.service';
import { Subscription } from 'rxjs';
import { Lightbox } from 'ngx-lightbox';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { GalleryService } from 'src/app/services/gallery.service';

@Component({
  selector: 'app-album-view-edit-mode',
  templateUrl: './album-view-edit-mode.component.html',
  styleUrls: ['./album-view-edit-mode.component.css']
})

export class AlbumViewEditModeComponent {

  loading:boolean=true;
  images:any[]=[];
  coverImage:any = null;
  showUploadHint: boolean = true;
  albumId: any;
  albumName: any;
  albumDescription: any;
  albumLenght: any;


  isEditMode = false;
  selectedImageIds: number[] = [];
  editImageId:number=-1;
  editImageOriginalValues:any= {};
  modalResponse: string = '';
  classModal:string='text-center mb-4'

  private imagesSubscription: Subscription = new Subscription();

  constructor(private albumsService:AlbumsService, private galleryService:GalleryService,  private router: Router, private globalDataService:GlobalDataService, private _lightbox: Lightbox, private toastr: ToastrService, private route: ActivatedRoute){
  }

  goAlbum(){
    this.router.navigate(["/profile/album", this.albumId]);
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
  }

  deleteSelectedImages() {
    //this.albumsService.deletePhoto
    this.isEditMode = false;
    this.classModal='text-center mb-4 '
    this.selectedImageIds = [];
  }

  toggleEditMode(id: number) {
    if (this.isEditMode) {
      const index = this.selectedImageIds.indexOf(id);
      if (index === -1) {
        this.selectedImageIds.push(id);
      } else {
        this.selectedImageIds.splice(index, 1);
      }
    } else {
      this.isEditMode = true;
      this.selectedImageIds.push(id);
    }
  }

  cancelEditMode() {
    this.isEditMode = false;
    this.selectedImageIds = [];
  }

  getAlbum():void{
    this.images=[]
    this.albumsService.getAlbumById(this.albumId).subscribe(
      (response)=>{

        if (response.body && Array.isArray(response.body)) {
          let len = response.body.length;
          response.body.forEach((element: any) => {
            if (element.data) {
              if(element.photoName != "defaultImage"){
                if (response.body.indexOf(element) == 1){
                  this.coverImage = {
                    "src":`data:image/${element.photoExtensio};base64,${element.data}`,
                    "id": element.photoId, "name":element.photoName, "description": element.photoDescription
                  }
                }
                this.images.unshift({
                  "src":`data:image/${element.photoExtensio};base64,${element.data}`,
                  "id": element.photoId, "name":element.photoName, "description": element.photoDescription
                });
                      
              }
            }
          });
        }
        this.albumsService.setImagesToAlbum(this.images);
        this.albumsService.getAlbumById(this.albumId).subscribe(
          (response)=>{
            if (response.body && Array.isArray(response.body)) {
              console.log(response.body[0]);
              this.albumName = response.body[0].albums[0].albumName;
              this.albumDescription = response.body[0].albums[0].description;
              this.albumLenght = response.body.length-1+ " fotos";
            }
            this.loading=false;
          }
          
        );
      },(error)=>{
        console.log('error al obtener album', error)
      }
      
    );
   
  }

  open(index: number): void {
    this._lightbox.open(this.images, index);
  }

  close(): void {
    this._lightbox.close();
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.albumId = params['id'];
    });  

    this.imagesSubscription = this.albumsService.getImagesObservable().subscribe((images) => {
      this.images = images;
    });

    this.getAlbum();
  }

  ngOnDestroy(): void {
    this.imagesSubscription.unsubscribe();
  }


}

