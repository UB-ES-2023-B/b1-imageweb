import { Component, Input } from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Subscription} from 'rxjs';
import {AlbumsService} from '../../services/albums.service';
import {GlobalDataService} from 'src/app/services/global-data.service';
import {ToastrService} from 'ngx-toastr';
import {Router} from '@angular/router';


@Component({
  selector: 'app-edit-albums',
  templateUrl: './edit-albums.component.html',
  styleUrls: ['./edit-albums.component.css']
})
export class EditAlbumsComponent {
  original_username: string = this.globalDataService.getUsername();
  loading: boolean = true;
  albums: any[] = [];
  editAlbumId:number=-1;
  editAlbumsOriginalValues:any= {};
  remainingWords: number = 25;
  isEditMode = false;
  classModal:string='text-center mb-4'
  selectedAlbumsIds: number[] = [];
  modalResponse: string = '';

  private albumsSubscription: Subscription = new Subscription();

  constructor(private modalService: NgbModal,
              private albumsService: AlbumsService,
              private toastr: ToastrService,
              private router: Router,
              private globalDataService: GlobalDataService) {
  }


  ngOnInit(): void {
      this.albumsSubscription = this.albumsService.getAlbumsObservable().subscribe((albums) => {
          this.albums = albums;
      });
      this.getAlbums();
  }

  startEditInfo(id:number, name:string, description:string){
    this.editAlbumId=id;
    this.editAlbumsOriginalValues={name:name,description:description}
  }
  cancelEditMode() {
    this.isEditMode = false;
    this.selectedAlbumsIds = [];
  }
  changeIsModal(){
    this.classModal='text-center mb-4 hideText'
  }
  cancelEditInfo(id:number){
    let index = this.albums.findIndex(imagen => imagen.id === id);

    if (index !== -1) {
      this.albums[index].name = this.editAlbumsOriginalValues.name;
      this.albums[index].description = this.editAlbumsOriginalValues.description;

    } else {
      console.log('Error en cancelar');
    }
    this.editAlbumId=-1;
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

  deleteSelectedImages() {
     //Falta endpoint
    this.albumsService.deleteAlbums( this.selectedAlbumsIds).subscribe(
      (response)=>{
        console.log('Se ha borrado bien', response)
        this.getAlbums()
      },
      (error)=>{
        console.log('error al eliminar', error)
        this.toastr.error('No se ha eliminado correctamente','Error');
      }
    )

    this.isEditMode = false;
    this.classModal='text-center mb-4 '
    this.selectedAlbumsIds = [];
  }

  toggleEditMode(id: number) {

    if (this.isEditMode) {
      // Si ya estamos en modo de edición, significa que se hizo clic en el checkbox
      const index = this.selectedAlbumsIds.indexOf(id);
      if (index === -1) {
        // Si el ID no está en la lista, lo añadimos
        this.selectedAlbumsIds.push(id);
      } else {
        // Si el ID ya está en la lista, lo eliminamos
        this.selectedAlbumsIds.splice(index, 1);
      }
    } else {
      // Si no estamos en modo de edición, activamos el modo y añadimos la imagen actual
      this.isEditMode = true;
      this.selectedAlbumsIds.push(id);
    }
    // console.log('Lista de imágenes seleccionadas:', this.selectedImageIds);
  }
  countWords(description:string, index:number) {
    const words = description.split(/\s+/).filter((word) => word.length > 0);
    const remainingWords = 25 - words.length;

    if (remainingWords >= 0) {
        this.remainingWords = remainingWords;
    } else {
        // Limitar el número de palabras en el textarea
        const limit = words.slice(0, 25).join(' ');
        this.albums[index]
        this.albums[index].description = limit;
        this.remainingWords = 0;
    }
    }

  saveEditInfo(id:number, name:string, description:string){
    this.albumsService.updateInfoAlbum(id,name, description).subscribe(
      (response)=>{
        this.toastr.success('Se ha editado correctamente');
        this.editAlbumId=-1;
      },
      (error)=>{
        console.log('error editar campos de albums', error)
        this.toastr.error('No se ha editado correctamente','Error');
        let index = this.albums.findIndex(album => album.id === id);

        if (index !== -1) {
          this.albums[index].name = this.editAlbumsOriginalValues.name;
          this.albums[index].description = this.editAlbumsOriginalValues.description;

        } else {
          console.log('Error en cancelar en albums');
        }
        this.editAlbumId=-1;

      }
    )
  }

  goGallery(){
    this.router.navigate(['/profile/']);
  }

  getAlbums() {
      this.albums = []
      this.albumsService.getAlbumsForUser(this.globalDataService.getUsername()).subscribe(
          (response) => {
            if (response.body) {
              response.body.forEach((element: any) => {
                let src = `data:image/${element.coverPhoto.photoExtension};base64,${ element.coverPhoto.data}`;
                this.albums.unshift({
                  "src": src,
                  "id": element.albumId,
                  "name": element.name,
                  "description": element.description,
                  "photoLength": element.length-1
                });
              });
            }
            this.albumsService.setAlbums(this.albums);
            this.loading = false;

          },
          (error) => {
              console.log('error al obtener all gallery', error)
              this.loading = false;
          }
      )
  }

  ngOnDestroy(): void {
      this.albumsSubscription.unsubscribe();
  }
}
